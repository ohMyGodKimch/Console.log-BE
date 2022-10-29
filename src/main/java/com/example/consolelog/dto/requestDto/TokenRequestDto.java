package com.example.consolelog.dto.requestDto;

import lombok.Getter;

@Getter
public class TokenRequestDto {

    private String accessToken;
    private String refreshToken;
}
