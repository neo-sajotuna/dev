package com.sparta.newneoboardbuddy.domain.card.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.exception.InvalidRequestException;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardCreateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardCreateResponse;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.member.service.MemberService;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

//    private final BoardListRepository boardListRepository;
    private final CardRepository cardRepository;
    private final MemberService memberService;

    public CardCreateResponse createCard(Long listId, AuthUser authUser, CardCreateRequest request) {
        User user = User.fromUser(authUser);

         memberService.memberPermission(authUser, user.getId(), request.getWorkspaceId());

        // 읽기 전용 유저 생성 못하게 예외처리

//        BoardList list = boardListRepository.findById(listId).orElseThrow(() ->
//                new InvalidRequestException("list not found"));

        BoardList list = null;

        Card newCard = new Card(
                request.getCardTitle(),
                request.getCardContent(),
                request.getFinishedAt(),
//                request.getMember(),  얘 어떡할건데
                user,
                list
        );
        Card savedCard = cardRepository.save(newCard);

        return new CardCreateResponse(
                savedCard.getCardId(),
                savedCard.getCardTitle(),
                savedCard.getCardContent(),
                savedCard.getFinishedAt()
        );

    }
}
