package com.sparta.newneoboardbuddy.domain.workspace.dto.request;

import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import lombok.Getter;

@Getter
public class InviteMemberRequest {
    private String email;
    private MemberRole memberRole;

}
