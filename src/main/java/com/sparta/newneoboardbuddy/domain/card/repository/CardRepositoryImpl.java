package com.sparta.newneoboardbuddy.domain.card.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.entity.QCard;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;



@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Card> searchCards(String cardTitle, String cardContent, String member, String finishedAt, Pageable pageable){

        QCard card = QCard.card;

        BooleanBuilder builder = new BooleanBuilder();


        if (cardTitle != null && !cardTitle.isEmpty()) {
        }


    }
}
