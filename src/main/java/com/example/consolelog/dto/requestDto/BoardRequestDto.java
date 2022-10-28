package com.example.consolelog.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class BoardRequestDto {

    private String title;

    private String content;

    //S3 구현 시 사용
//    private List<String> images;
}
