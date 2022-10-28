package com.example.consolelog.controller;


import com.example.consolelog.dto.requestDto.MemberReqeustDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequestMapping("/member")
public class MemberController {

    @PostMapping("/signup")
    public ResponseDto<?> signup(@RequestBody MemberReqeustDto memberReqeustDto) {
        return null;
    }
}
