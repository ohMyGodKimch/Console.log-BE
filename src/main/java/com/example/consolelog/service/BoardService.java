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
import com.example.consolelog.repository.CommentRepository;
import com.example.consolelog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final Time time;


    public ResponseDto<?> createBoard(BoardRequestDto boardRequestDto, MemberDetailsImpl memberDetails) {
        // 로그인한 멤버가 누구인지 확인
        Member member = memberRepository.findByName(memberDetails.getUsername()).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        Board board = new Board(boardRequestDto, member);

        boardRepository.save(board);

        return ResponseDto.success(new BoardResponseDto(board, time));
    }

    // 게시물 전체 조회
    public ResponseDto<?> getBoardList() {

        List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        for (Board board : boardList) {
            boardResponseDtoList.add(new BoardResponseDto(board, time));
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

        return ResponseDto.success(new BoardResponseDto(board, time, commentResponseDtoList));

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
    public ResponseDto<?> updateBoard(Long boardId, BoardRequestDto boardRequestDto, MemberDetailsImpl memberDetails) {

        memberRepository.findByName(memberDetails.getUsername()).orElseThrow(() -> new NullPointerException("해당 사용자의 정보가 없습니다."));

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        board.update(boardRequestDto);

        return ResponseDto.success(new BoardResponseDto(board, time));
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deleteBoard(Long boardId, MemberDetailsImpl memberDetails) {

        memberRepository.findByName(memberDetails.getUsername()).orElseThrow(() -> new NullPointerException("해당 사용자의 정보가 없습니다."));

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        boardRepository.delete(board);

        return ResponseDto.success("게시글 삭제 완료");
    }
}
