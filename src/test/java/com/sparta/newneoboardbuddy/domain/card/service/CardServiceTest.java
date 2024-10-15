package com.sparta.newneoboardbuddy.domain.card.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.board.repository.BoardRepository;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardCreateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardCreateResponse;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.repository.CardActivityLogRepository;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.list.repository.BoardListRepository;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
import com.sparta.newneoboardbuddy.domain.member.service.MemberService;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private BoardListRepository boardListRepository;

    @Mock
    private CardActivityLogRepository cardActivityLogRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    void createCard() {
        // given
        AuthUser authUser = new AuthUser(1L, "gusrnr5153@naver.com", UserRole.ROLE_ADMIN);
        User user = User.fromUser(authUser);
        Long workspaceId = 1L;
        Long listId = 1L;

        Member member = new Member();
        member.setMemberRole(MemberRole.WORKSPACE_MEMBER);  // ADMIN 역할을 설정하여 읽기 전용 멤버가 아님을 명확히 함
        given(memberService.memberPermission(authUser, workspaceId)).willReturn(member);

        BoardList list = new BoardList();
        given(boardListRepository.findById(listId)).willReturn(Optional.of(list));

        Long assignMemberId = 1L;
        CardCreateRequest request = new CardCreateRequest(workspaceId, "dkdk", "아아아", LocalTime.now().plusHours(10), LocalTime.now().plusHours(20), assignMemberId);

        Member assignedMember = new Member(); // 실제 카드에 할당될 담당자 멤버 생성
        assignedMember.setMemberId(assignMemberId); // assignMemberId 설정
        given(memberRepository.findById(assignMemberId)).willReturn(Optional.of(assignedMember));

        Card card = new Card("dkdk", "아아아", LocalTime.now().plusHours(10), LocalTime.now().plusHours(20), assignedMember, user, list);
        given(cardRepository.save(any(Card.class))).willReturn(card);

        // when
        CardCreateResponse response = cardService.createCard(listId, authUser, request);

        // then
        assertNotNull(response);
        assertEquals("dkdk", response.getCardTitle());
        assertEquals(assignMemberId, response.getMemberId()); // 할당된 멤버 ID가 일치하는지 검증
        verify(cardRepository, times(1)).save(any(Card.class));
    }


    @Test
    void updateCard() {
    }

    @Test
    void getCardDetails() {
    }

    @Test
    void deleteCard() {
    }

    @Test
    void searchCards() {
    }
}