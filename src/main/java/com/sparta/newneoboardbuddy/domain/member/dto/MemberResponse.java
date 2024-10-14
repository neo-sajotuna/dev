package com.sparta.newneoboardbuddy.domain.member.dto;

import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberResponse {
    private final Long memberId;
    private final MemberRole memberRole;
}
