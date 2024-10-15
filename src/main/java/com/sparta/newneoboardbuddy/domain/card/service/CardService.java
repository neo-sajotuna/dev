package com.sparta.newneoboardbuddy.domain.card.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.exception.NotFoundException;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardCreateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardUpdateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardCreateResponse;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardDetailResponse;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardUpdateResponse;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.entity.CardActivityLog;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.enums.Action;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.repository.CardActivityLogRepository;
import com.sparta.newneoboardbuddy.domain.comment.entity.Comment;
import com.sparta.newneoboardbuddy.domain.file.dto.request.FileUploadDto;
import com.sparta.newneoboardbuddy.domain.file.service.FileService;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
import com.sparta.newneoboardbuddy.domain.member.service.MemberService;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

//    private final BoardListRepository boardListRepository;
    private final CardRepository cardRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CardActivityLogRepository cardActivityLogRepository;
    private final FileService fileService;

    @Transactional
    public CardCreateResponse createCard(Long listId, AuthUser authUser, CardCreateRequest request, MultipartFile file) {
        User user = User.fromUser(authUser);

        // 혹시 모르니 이부분은 안바꾸겠습니다.
        Member member = memberService.memberPermission(authUser, request.getWorkspaceId());

        // 카드 추가될 리스트 조회
        BoardList list = boardListRepository.findById(listId).orElseThrow(() ->
                new InvalidRequestException("list not found"));

        // 담당자 멤버 ID 를 받아서 조회
         Member assignedMember = memberRepository.findById(request.getMemberId()).orElseThrow(()-> new NotFoundException("멤버가 없습니다."));


        Card newCard = new Card(
                request.getCardTitle(),
                request.getCardContent(),
                request.getStartedAt(),
                request.getFinishedAt(),
                assignedMember,
                user,
                list
        );
        Card savedCard = cardRepository.save(newCard);

        // 파일 저장 로직
        FileUploadDto fileUploadDto = FileUploadDto.builder()
                .targetId(savedCard.getCardId())
                .targetTable("card")
                .targetFile(file)
                .build();

        fileService.uploadFile(fileUploadDto);
        // 파일 저장 로직

        logCardActivity(savedCard, Action.CREATED, "제목: " + savedCard.getCardTitle()  +
                ", 내용 : " + savedCard.getCardContent() +
                ", 관리 멤버 :" + " -> " + assignedMember);

        return new CardCreateResponse(
                savedCard.getCardId(),
                savedCard.getCardTitle(),
                savedCard.getCardContent(),
                savedCard.getStartedAt(),
                savedCard.getFinishedAt(),
                savedCard.getMember().getMemberId()
        );

    }


    public CardUpdateResponse updateCard(Long cardId, AuthUser authUser, CardUpdateRequest request) {
        Card card = cardRepository.findById(cardId).orElseThrow(()->
                new NoSuchElementException("카드를 찾을 수 없다."));

        // 수정 전 현재 상태
        String oldTitle = card.getCardTitle();
        String oldContent = card.getCardContent();

        // 카드 수정
        card.setCardTitle(request.getCardTitle());
        card.setCardContent(request.getCardContent());

        if(request.getMemberId() != null){
            Member assignedMember = memberRepository.findById(request.getMemberId())
                    .orElseThrow(()-> new NotFoundException("멤버가 없습니다."));
            card.setMember(assignedMember);
        }

        Card updateCard = cardRepository.save(card);

        logCardActivity(updateCard, Action.UPDATED, "제목: " + oldTitle + " -> " + updateCard.getCardTitle() +
                ", 내용 : " + oldContent + " -> " + updateCard.getCardContent() +
                ", 관리 멤버 :" + " -> " + updateCard.getMember().getMemberId());


        return new CardUpdateResponse(updateCard.getCardId(), updateCard.getCardTitle(), updateCard.getCardContent(), updateCard.getMember().getMemberId(), updateCard.getActivatedAt());

    }

    private void logCardActivity(Card card, Action action, String details) {
        CardActivityLog activityLog = new CardActivityLog();
        activityLog.setCard(card);
        activityLog.setAction(action);
        activityLog.setDetails(details);
        activityLog.setActiveTime(LocalDateTime.now());

        cardActivityLogRepository.save(activityLog);
    }

//    @Transactional(readOnly = true)
    public CardDetailResponse getCardDetails(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new NotFoundException("카드가 없다."));

        // 카드 활동 내역 조회
        List<CardActivityLog> activityLogs = cardActivityLogRepository.findByCard(card);

        // 카드 댓글 조회
        List<Comment> comments = card.getComments();

        return new CardDetailResponse(
                card.getCardId(),
                card.getCardTitle(),
                card.getCardContent(),
                card.getStartedAt(),
                card.getFinishedAt(),
                activityLogs,
                comments
        );
    }
}
