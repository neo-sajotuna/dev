package com.sparta.newneoboardbuddy.domain.cardActivityLog.repository;

import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.entity.CardActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CardActivityLogRepository extends JpaRepository<CardActivityLog, Long> {

    List<CardActivityLog> findByCard(Card card);
    Optional<CardActivityLog> findTopByCardOrderByActiveTimeDesc(Card card);

    Optional<CardActivityLog> findFirstByCard_CardIdOrderByActiveTimeDesc(Long cardId);

}
