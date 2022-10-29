package com.example.consolelog.controller;

import com.example.consolelog.dto.requestDto.CommentRequestDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.service.CommentService;
import com.example.consolelog.service.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping(value = "/{board_id}/comments")
    public ResponseDto<?> createComment(@PathVariable Long board_id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return commentService.createComment(board_id, commentRequestDto, memberDetails);
    }

    // 댓글 수정
    @PutMapping(value = "/comments/{comment_id}")
    public ResponseDto<?> updateComment(@PathVariable Long comment_id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return commentService.updateComment(comment_id, commentRequestDto, memberDetails);
    }

    // 댓글 삭제
    @DeleteMapping(value = "/comments/{comment_id}")
    public ResponseDto<?> deleteComment(@PathVariable Long comment_id, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return commentService.deleteComment(comment_id, memberDetails);
    }

}