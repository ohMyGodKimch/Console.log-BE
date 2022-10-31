package com.example.consolelog.repository;

import com.example.consolelog.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    List<Board> findAllByOrderByCreatedAtDesc();
}
