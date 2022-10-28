package com.example.consolelog.service;


import com.example.consolelog.dto.requestDto.MemberReqeustDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public ResponseDto<?> checkName(String name) {

        if (memberRepository.existsByName(name))
            return ResponseDto.success(false);

        return ResponseDto.success(true);
    }

    public ResponseDto<?> checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname))
            return ResponseDto.success(false);

        return ResponseDto.success(true);
    }
}
