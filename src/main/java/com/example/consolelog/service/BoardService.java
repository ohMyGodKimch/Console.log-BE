package com.example.consolelog.service;

import com.example.consolelog.dto.requestDto.BoardRequestDto;
import com.example.consolelog.dto.responseDto.BoardResponseDto;
import com.example.consolelog.dto.responseDto.CommentResponseDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Comment;
import com.example.consolelog.entity.Member;
import com.example.consolelog.entity.Time;
import com.example.consolelog.repository.BoardRepository;
import com.example.consolelog.repository.BoardRepositoryImpl;
import com.example.consolelog.repository.CommentRepository;
import com.example.consolelog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardRepositoryImpl boardRepositoryImpl;


    public ResponseDto<?> createBoard(BoardRequestDto boardRequestDto, Member member) {
        // 로그인한 멤버가 누구인지 확인

        Board board = new Board(boardRequestDto, member);

        boardRepository.save(board);

        return ResponseDto.success(new BoardResponseDto(board));
    }

    // 게시물 전체 조회
    public ResponseDto<?> getBoardList() {

        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        for (Board board : boardList) {
            boardResponseDtoList.add(new BoardResponseDto(board));
        }

        return ResponseDto.success(boardResponseDtoList);
    }


    // 게시글 상세 조회
    public ResponseDto<?> getBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        List<Comment> commentList = commentRepository.findAllByBoardOrderByCreatedAtDesc(board);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .comment(comment.getContent())
                    .nickname(comment.getMember().getNickname())
                    .dayBefore(Time.calculateTime(comment))
                    .build()
            );
        }
        return ResponseDto.success(BoardResponseDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getMember().getNickname())
                .commentCount(board.getCommentList().size())
                .heartCount(board.getHeartList().size())
                .commentList(commentResponseDtoList)
                .dayBefore(Time.calculateTime(board))
                .build());

//        return ResponseDto.success(new BoardResponseDto(board, commentResponseDtoList));

//        return ResponseDto.success(BoardResponseDto.builder()
//                .boardId(board.getId())
//                .title(board.getTitle())
//                .content(board.getContent())
//                .writer(board.getMember().getNickname())
//                .commentList(commentResponseDtoList)
//                .commentCount(board.getCommentList().size())
//                .heartCount(board.getHeartList().size())
//                .build()
//        );

    }

    // 게시글 수정
    @Transactional
    public ResponseDto<?> updateBoard(Long boardId, BoardRequestDto boardRequestDto, Member member) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        if (validateMember(member, board))
            throw new IllegalArgumentException("게시물 작성자와 현재 사용자가 일치하지 않습니다.");

        board.update(boardRequestDto);

        return ResponseDto.success(new BoardResponseDto(board));
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deleteBoard(Long boardId, Member member) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        if (validateMember(member, board))
            throw new IllegalArgumentException("게시물 작성자와 현재 사용자가 일치하지 않습니다.");

        boardRepository.delete(board);

        return ResponseDto.success("게시글 삭제 완료");
    }

    // 게시물 트렌딩정렬 ( 생성날짜 & 하트 개수 기준 정렬하기)
    public ResponseDto<?> getTrendingBoard(String options) {

        List<Board> trendingBoard = new ArrayList<>();
        LocalDateTime currentDateTile = LocalDateTime.now();
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();


            // 오늘 기준
        if (options.equals("today")) {
            //게시물 생성 하루 기준 추출
            trendingBoard = boardRepository.findDistinctByCreatedAtBetween(currentDateTile.minusHours(24), LocalDateTime.now());

            //한 주 기준
        } else if (options.equals("week")) {
            trendingBoard = boardRepository.findDistinctByCreatedAtBetween(currentDateTile.minusWeeks(1), LocalDateTime.now());
            //(currentDateTile.minusWeeks(1), LocalDateTime.now());

            //한 달 기준
        } else if (options.equals("month")) {
            trendingBoard = boardRepository.findDistinctByCreatedAtBetween(currentDateTile.minusMonths(1), LocalDateTime.now());
            //(currentDateTile.minusMonths(1), LocalDateTime.now());
        }

        // 하트 개수 별로 정렬하기
        Collections.sort(trendingBoard, new Comparator<Board>() {
            @Override
            public int compare(Board o1, Board o2) {
                if (o1.getHeartList().size() > o2.getHeartList().size()) {
                    return -1;
                } else if (o1.getHeartList().size() < o2.getHeartList().size()) {
                    return 1;
                }
                return 0;
            }
        });

        for(Board board : trendingBoard) {
            boardResponseDtoList.add(new BoardResponseDto(board));
        }

        return ResponseDto.success(boardResponseDtoList);
    }



    public boolean validateMember(Member member, Board board) {
        return !member.getName().equals(board.getMember().getName());
    }

    public Slice<BoardResponseDto> getBoardListInfinite(Pageable pageable) {

            return boardRepositoryImpl.getBoardScroll(pageable);
        }
    }


