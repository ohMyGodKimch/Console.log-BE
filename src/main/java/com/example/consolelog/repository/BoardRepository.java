package com.example.consolelog.repository;

import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByMemberOrderByCreatedAtDesc(Member member);
}
