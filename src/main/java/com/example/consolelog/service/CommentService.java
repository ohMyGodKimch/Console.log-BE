package com.example.consolelog.service;

import com.example.consolelog.dto.requestDto.CommentRequestDto;
import com.example.consolelog.dto.responseDto.CommentResponseDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Comment;
import com.example.consolelog.repository.BoardRepository;
import com.example.consolelog.repository.CommentRepository;
import com.example.consolelog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // 댓글 생성
    public ResponseDto<?> createComment(Long boardId, CommentRequestDto commentRequestDto, MemberDetailsImpl memberDetails) {

        memberRepository.findByName(memberDetails.getUsername()).orElseThrow(()
                -> new NullPointerException("해당 사용자의 정보가 존재하지 않습니다."));

        Board board = boardRepository.findById(boardId).orElseThrow(()
                -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        Comment comment = new Comment(commentRequestDto, memberDetails.getMember(), board);

        commentRepository.save(comment);

        return ResponseDto.success(new CommentResponseDto(comment));
    }

    // 댓글 수정
    @Transactional
    public ResponseDto<?> updateComment(Long commentId, CommentRequestDto commentRequestDto, MemberDetailsImpl memberDetails) {

        memberRepository.findByName(memberDetails.getUsername()).orElseThrow(()
                -> new NullPointerException("해당 사용자의 정보가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        comment.update(commentRequestDto);

        return ResponseDto.success(new CommentResponseDto(comment));
    }

    // 댓글 삭제
    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, MemberDetailsImpl memberDetails) {

        memberRepository.findByName(memberDetails.getUsername()).orElseThrow(()
                -> new NullPointerException("해당 사용자의 정보가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        commentRepository.delete(comment);

        return ResponseDto.success("댓글 삭제 완료");
    }
}
