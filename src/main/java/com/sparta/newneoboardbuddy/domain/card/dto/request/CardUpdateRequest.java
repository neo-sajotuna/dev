package com.sparta.newneoboardbuddy.domain.card.dto.request;

import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardUpdateRequest {
    private String cardTitle;
    private String cardContent;
    private Long memberId;
    private LocalTime activeTime;
}
