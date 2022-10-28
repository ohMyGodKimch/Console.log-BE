package com.example.consolelog.controller;


import com.example.consolelog.dto.requestDto.MemberReqeustDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseDto<?> signup(@RequestBody MemberReqeustDto memberReqeustDto) {

        return memberService.signup(memberReqeustDto);
    }

    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody MemberReqeustDto memberReqeustDto, HttpServletResponse response) {

        return memberService.login(memberReqeustDto, response);
    }

    @PostMapping("/check-name")
    public ResponseDto<?> checkName(String name) {

        return memberService.checkName(name);
    }

    @PostMapping("/check-nickname")
    public ResponseDto<?> checkNickname(String nickname) {

        return memberService.checkNickname(nickname);
    }

}
