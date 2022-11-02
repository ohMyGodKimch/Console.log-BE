package com.example.consolelog.dto.responseDto;

import com.example.consolelog.entity.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {

    private T data;
    private Error error;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>( data, null);
    }

    public static <T> ResponseDto<T> fail(String code, String message){
        return new ResponseDto<>(null, new Error(code, message));
    }
}