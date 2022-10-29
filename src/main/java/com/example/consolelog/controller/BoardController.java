package com.example.consolelog.controller;

import com.example.consolelog.dto.requestDto.BoardRequestDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.service.BoardService;
import com.example.consolelog.service.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    // 게시물 생성
    @PostMapping(value = "")
    public ResponseDto<?> createBoard(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return boardService.createBoard(boardRequestDto,memberDetails);
    }

    // 게시물 전체 조회
    @GetMapping(value = "")
    public ResponseDto<?> getBoardList(){
        return boardService.getBoardList();
    }

    // 게시물 상세 조회
    @GetMapping(value = "/{board_id}")

    public ResponseDto<?> getBoard(@PathVariable(name = "board_id") Long boardId){
        return boardService.getBoard(boardId);

    }

    // 게시물 수정
    @PutMapping(value = "/{board_id}")
    public ResponseDto<?> updateBoard(@PathVariable(name = "board_id") Long boardId, @RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return boardService.updateBoard(boardId, boardRequestDto, memberDetails);

    }

    // 게시글 삭제
    @DeleteMapping(value = "/{board_id}")

    public ResponseDto<?> deleteBoard(@PathVariable(name = "board_id") Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return boardService.deleteBoard(boardId, memberDetails);

    }


}
