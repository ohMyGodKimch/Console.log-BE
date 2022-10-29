package com.example.consolelog.dto.responseDto;

import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Time;
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

    private int commentCount = 0;

    private int heartCount = 0;

    public BoardResponseDto(Board board, Time time){
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.dayBefore = time.calculateTime(board);
        this.writer = board.getMember().getNickname();
        if (board.getCommentList() != null || board.getHeartList() != null){
            this.commentCount = board.getCommentList().size();
            this.heartCount = board.getHeartList().size();
        }


    }

}
