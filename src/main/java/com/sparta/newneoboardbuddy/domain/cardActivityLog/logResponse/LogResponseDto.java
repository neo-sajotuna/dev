package com.sparta.newneoboardbuddy.domain.cardActivityLog.logResponse;

import com.sparta.newneoboardbuddy.domain.cardActivityLog.entity.CardActivityLog;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.enums.Action;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
public class LogResponseDto {
    Long cardActivityLogId;
    Action action;
    LocalTime activeTime;

    public LogResponseDto(CardActivityLog cardActivityLog) {
        this.cardActivityLogId = cardActivityLog.getCardActivityLogId();
        this.action = cardActivityLog.getAction();
        this.activeTime = cardActivityLog.getActiveTime();
    }

}
