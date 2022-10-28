package com.example.consolelog.service;


import com.example.consolelog.dto.TokenDto;
import com.example.consolelog.dto.requestDto.MemberReqeustDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.entity.Member;
import com.example.consolelog.entity.RefreshToken;
import com.example.consolelog.repository.MemberRepository;
import com.example.consolelog.repository.RefreshTokenRepository;
import com.example.consolelog.security.JwtFilter;
import com.example.consolelog.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refeshTokenRepository;

    public ResponseDto<?> signup(MemberReqeustDto memberReqeustDto) {

        Member member = new Member(memberReqeustDto, passwordEncoder.encode(memberReqeustDto.getPassword()));

        memberRepository.save(member);

        return ResponseDto.success("회원가입 성공했습니다.");
    }

    public ResponseDto<?> login(MemberReqeustDto memberRequestDto, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Member member = memberRepository.findByName(authentication.getName()).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(memberRequestDto.getPassword(), member.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다."); // 커스텀 예외 처리 예정

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refeshTokenRepository.save(refreshToken);

        response.setHeader(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());

        return ResponseDto.success("로그인에 성공했습니다.");
    }

    public ResponseDto<?> checkName(String name) {

        if (memberRepository.existsByName(name))
            return ResponseDto.success(false); // 예외처리 예정

        return ResponseDto.success(true);
    }

    public ResponseDto<?> checkNickname(String nickname) {

        if (memberRepository.existsByNickname(nickname))
            return ResponseDto.success(false); // 예외처리 예정

        return ResponseDto.success(true);
    }
}
