package com.sparta.newneoboardbuddy.domain.member.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import com.sparta.newneoboardbuddy.domain.workspace.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;


    public Member memberPermission(AuthUser authUser, Long workspaceId) {
        User user = User.fromUser(authUser);

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new NoSuchElementException("워크스페이스 없다"));

        return memberRepository.findByUserAndWorkspace(user,workspace)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
    }
}
