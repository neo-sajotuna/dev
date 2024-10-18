package com.sparta.newneoboardbuddy.domain.card.repository;

import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardRepositoryCustom {

    Page<Card> searchCards(String cardTitle, String cardContent, Long assignedMemberId, Long boardId, Pageable pageable);
    Page<Card> searchCards(Long userId, Pageable pageable);

    List<Card> findByCardTitle(String title);
}
