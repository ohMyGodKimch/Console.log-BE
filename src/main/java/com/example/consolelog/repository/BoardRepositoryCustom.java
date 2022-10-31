package com.example.consolelog.repository;

import com.example.consolelog.dto.responseDto.BoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardRepositoryCustom {
    Page<BoardResponseDto> getBoardPaging(Pageable pageable);

    Slice<BoardResponseDto> getBoardScroll(Pageable pageable);
}
