package com.example.consolelog.controller;

import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.service.HeartService;
import com.example.consolelog.service.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/{board_id}")
    public ResponseDto<?> addHeart(@PathVariable Long board_id, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return heartService.addHeart(board_id, memberDetails);
    }

    @DeleteMapping("/{board_id}")
    public ResponseDto<?> cancelHeart(@PathVariable Long board_id, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return heartService.cancelHeart(board_id, memberDetails);
    }

}
