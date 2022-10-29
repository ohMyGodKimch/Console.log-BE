package com.example.consolelog.service;

import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Heart;
import com.example.consolelog.entity.Member;
import com.example.consolelog.repository.BoardRepository;
import com.example.consolelog.repository.HeartRepository;
import com.example.consolelog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;

    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;

    // 하트 생성 ♡!!
    public ResponseDto<?> addHeart(Long board_id, MemberDetailsImpl memberDetails) {

        Member member = memberRepository.findByName(memberDetails.getUsername()).orElseThrow(()
                -> new NullPointerException("해당 사용자의 정보가 없습니다."));

        Board board = boardRepository.findById(board_id).orElseThrow(()
                -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        if ( ! heartRepository.existsByBoardAndMember(board, member)) {

            Heart heart = new Heart(board, member);
            heartRepository.save(heart);
        }

        return ResponseDto.success("좋아요를 눌렀습니다");

    }


    // 하트 취소 ♡ !!
    public ResponseDto<?> cancelHeart(Long board_id, MemberDetailsImpl memberDetails) {

        Member member = memberRepository.findByName(memberDetails.getUsername()).orElseThrow(()
                -> new NullPointerException("해당 사용자의 정보가 없습니다."));

        Board board = boardRepository.findById(board_id).orElseThrow(()
                -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        if (  heartRepository.existsByBoardAndMember(board, member)) {

            Heart heart = heartRepository.findByBoard(board);
            heartRepository.delete(heart);
        }

        return ResponseDto.success("좋아요를 취소했습니다");
    }
}
