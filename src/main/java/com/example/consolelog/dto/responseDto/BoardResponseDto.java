package com.example.consolelog.dto.responseDto;

import com.example.consolelog.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class BoardResponseDto {

    private Long boardId;

//    private String image;

    private String title;

    private String content;

    private String dayBefore;

//    private String userImage;

    private String writer;

    private int commentCount;

    private int heartCount;

    public BoardResponseDto(Board board, String dayBefore){
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.dayBefore = dayBefore;
        this.writer = board.getMember().getNickname();
        this.commentCount = board.getCommentList().size();
        this.heartCount = board.getHeartList().size();
    }

}
