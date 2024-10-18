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

    /**
     * 해당 타이틀을 가진 Card 객체들을 반환하는 메서드
     * @param title 조회할 Title 내용
     * @return 해당 타이틀을 가진 Card List
     */
    public List<Card> findByCardTitle(String title) {
        QCard card = QCard.card;

        return queryFactory.selectFrom(card)
                .where(card.cardTitle.eq(title))
                .fetch();
    }

    /**
     * Null이 아닌 조건 내용 모두 포함하고 있는 카드들을 페이징하여 반환하는 메서드
     * @param cardTitle Card.title에 cardTitle의 내용이 들어 있는지 확인할 문자열
     * @param cardContent Card.content에 cardContent의 내용이 들어 있는지 확인할 문자열
     * @param assignedMemberId Card.userId에 assignedMemberId와 일치 여부를 확인할 Card와 동일한 Space에 소속인 UserId
     * @param boardId Card.board.boardId와 일치 여부를 확인할 boardId
     * @param pageable 페이징 조건
     * @return Null이 아닌 조건 모두 만족하는 페이징 된 카드
     */
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
}
