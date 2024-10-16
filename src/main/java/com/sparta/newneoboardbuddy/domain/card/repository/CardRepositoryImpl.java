package com.sparta.newneoboardbuddy.domain.card.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.newneoboardbuddy.common.exception.InvalidRequestException;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.entity.QCard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Card> searchCards(String cardTitle, String cardContent, Long assignedMemberId , Long boardId, Pageable pageable){

        QCard card = QCard.card;


        BooleanBuilder builder = new BooleanBuilder();


        if (cardTitle != null && !cardTitle.isEmpty()) {
            builder.and(card.cardTitle.containsIgnoreCase(cardTitle));
        }
        if (cardContent != null && !cardContent.isEmpty()) {
            builder.and(card.cardContent.containsIgnoreCase(cardContent));
        }
        if (assignedMemberId != null) {
            builder.and(card.member.memberId.eq(assignedMemberId));
        }
        if (boardId != null) {
            builder.and(card.board.boardId.eq(boardId));
        }


        Long total = queryFactory
                .select(card.count())
                .from(card)
                .where(builder)
                .fetchOne();

        if (total == null) {
            throw new NullPointerException("total is null");
        }

        // 카드 목록 페이징
        List<Card> cards = queryFactory.selectFrom(card)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(cards, pageable, total);

    }


    // 비관적 락
    @Override
    public Card findCardWithLock(Long cardId){
        return entityManager.find(Card.class, cardId, LockModeType.PESSIMISTIC_WRITE);
    }
}
