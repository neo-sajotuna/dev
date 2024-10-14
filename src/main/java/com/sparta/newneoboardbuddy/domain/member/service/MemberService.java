package com.sparta.newneoboardbuddy.domain.member.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.member.dto.MemberResponse;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;


    public MemberResponse getMember(Long workspaceId, Long memberId, AuthUser authUser){
       Member member = memberRepository.findByUserIdAndWorkspaceId(authUser, workspaceId)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));

        return new MemberResponse(
                member.getMemberId(),
                member.getMemberRole()
        );
    }
}
