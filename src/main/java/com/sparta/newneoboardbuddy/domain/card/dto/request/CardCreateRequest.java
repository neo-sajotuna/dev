package com.sparta.newneoboardbuddy.domain.card.dto.request;

import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardCreateRequest {
    private Long workspaceId;
    private String cardTitle;
    private String cardContent;
    private LocalTime startedAt;
    private LocalTime finishedAt;
    private Long memberId;
}
