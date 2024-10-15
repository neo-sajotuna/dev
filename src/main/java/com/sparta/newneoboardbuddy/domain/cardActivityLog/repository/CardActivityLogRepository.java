package com.sparta.newneoboardbuddy.domain.cardActivityLog.repository;

import com.sparta.newneoboardbuddy.domain.cardActivityLog.entity.CardActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardActivityLogRepository extends JpaRepository<CardActivityLog, Long> {
}
