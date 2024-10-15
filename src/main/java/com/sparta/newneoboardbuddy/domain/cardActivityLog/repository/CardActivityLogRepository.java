package com.sparta.newneoboardbuddy.domain.cardActivityLog.repository;

import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.entity.CardActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardActivityLogRepository extends JpaRepository<CardActivityLog, Long> {

    List<CardActivityLog> findByCard(Card card);
}
