package com.example.consolelog.dto.responseDto;

import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Image;
import com.example.consolelog.entity.Time;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class BoardResponseDto {

    private Long boardId;

    private String thumbnail;

    private String title;

    private String content;

    private String dayBefore;

    private String userImage;

    private String writer;

    private List<CommentResponseDto> commentList;

    private int commentCount = 0;

    private int heartCount = 0;

    // 게시물 목록 보기 & 게시물 등록 & 게시물 수정 사용
    public BoardResponseDto(Board board, Image image){
        this.boardId = board.getId();
        this.thumbnail = image.getImageUrl();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.dayBefore = Time.calculateTime(board);
        this.writer = board.getMember().getNickname();
        if (board.getCommentList() != null || board.getHeartList() != null){
            this.commentCount = board.getCommentList().size();
            this.heartCount = board.getHeartList().size();
        }
    }

    // 상세 게시물 보기 사용
    public BoardResponseDto(Board board, List<CommentResponseDto> commentResponseDtoList) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.dayBefore = Time.calculateTime(board);
        this.writer = board.getMember().getNickname();
        this.commentList = commentResponseDtoList;
        if (board.getCommentList() != null || board.getHeartList() != null){
            this.commentCount = board.getCommentList().size();
            this.heartCount = board.getHeartList().size();
        }
    }
}
