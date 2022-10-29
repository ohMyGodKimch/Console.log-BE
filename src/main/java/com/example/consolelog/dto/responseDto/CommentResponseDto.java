package com.example.consolelog.dto.responseDto;

import com.example.consolelog.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long commentId;

    private Long boardId;

    private String comment;

    private String nickname;


    public CommentResponseDto(Comment comment){
        this.commentId = comment.getId();
        this.boardId = comment.getBoard().getId();
        this.comment = comment.getContent();
        this.nickname = comment.getMember().getNickname();
    }

}
