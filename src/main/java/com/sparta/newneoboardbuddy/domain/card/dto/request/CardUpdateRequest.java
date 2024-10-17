package com.sparta.newneoboardbuddy.domain.card.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardUpdateRequest {
    private Long workspaceId;
    private String cardTitle;
    private String cardContent;
    private Long memberId;
    private LocalTime activeTime;
}
