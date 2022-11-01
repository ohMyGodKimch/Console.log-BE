package com.example.consolelog.repository;

import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Heart;
import com.example.consolelog.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HeartRepository extends JpaRepository<Heart ,Long> {

    boolean existsByBoardAndMember(Board board, Member member);

    Heart findByBoard(Board board);

    // 각 보드별 특정 기간동안 받은 하트 목록
    List<Heart> findAllByBoardCreatedAtBetween(Board board, LocalDateTime start, LocalDateTime end);
}
