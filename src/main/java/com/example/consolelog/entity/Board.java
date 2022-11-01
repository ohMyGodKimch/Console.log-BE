package com.example.consolelog.entity;

import com.example.consolelog.dto.requestDto.BoardRequestDto;
import com.example.consolelog.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;
//@ElementCollection
//    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "board")
    private List<Heart> heartList;




    public Board(BoardRequestDto boardRequestDto, Member member) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.member = member;
    }


    public void update(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
    }
}
