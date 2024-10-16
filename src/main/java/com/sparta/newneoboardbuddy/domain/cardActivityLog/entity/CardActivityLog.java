package com.sparta.newneoboardbuddy.domain.cardActivityLog.entity;

import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.enums.Action;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "CardActivityLog")
public class CardActivityLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardActivityLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Enumerated(EnumType.STRING)
    private Action action; // "Creation", "Doing", "Done", "Deleted"

    private LocalTime activeTime;

    private String details; // 수정된 내용
}
