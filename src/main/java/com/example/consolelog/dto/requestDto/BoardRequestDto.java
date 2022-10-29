package com.example.consolelog.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class BoardRequestDto {

    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "게시글 내용을 입력해주세요")
    private String content;

    //S3 구현 시 사용
//    private List<String> images;
}
