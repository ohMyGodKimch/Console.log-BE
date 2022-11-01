package com.example.consolelog.repository;

import com.example.consolelog.dto.responseDto.BoardResponseDto;
import com.example.consolelog.entity.*;
import com.example.consolelog.security.TokenProvider;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final HeartRepository heartRepository;
    private final TokenProvider tokenProvider;


    @Override
    public Page<BoardResponseDto> getBoardPaging(Pageable pageable) {
        return null;
    }

    @Override
    public Slice<BoardResponseDto> getBoardScroll(Pageable pageable) {
        QBoard board = QBoard.board;
        Member member = tokenProvider.getMemberFromAuthentication();
        List<Board> boardList = queryFactory
                .select(board)
                .from(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        for (Board eachBoard : boardList) {
//            Optional<Heart> heart = heartRepository.findByMemberAndBoard(member, eachBoard);
//            boolean heartByMe;
//            heartByMe = heart.isPresent();

            boardResponseDtoList.add(
                    BoardResponseDto.builder()
                            .boardId(eachBoard.getId())
                            .title(eachBoard.getTitle())
                            .content(eachBoard.getContent())
                            .writer(eachBoard.getMember().getNickname())
                            .dayBefore(Time.calculateTime(eachBoard))
                            .commentCount(eachBoard.getCommentList().size())
                            .heartCount(eachBoard.getHeartList().size())
                            .build()
            );
        }
        boolean hasNext = false;
        if (boardResponseDtoList.size() > pageable.getPageSize()) {
            boardResponseDtoList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(boardResponseDtoList, pageable, hasNext);
    }

    private OrderSpecifier<?> BoardSort(Pageable page) {
        QBoard board = QBoard.board;

        if (!page.getSort().isEmpty()) {
            for (Sort.Order order : page.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "createdAt":
                    case "descending":
                        return new OrderSpecifier(direction, board.createdAt);
                }

            }
        }

        return null;
    }
}
