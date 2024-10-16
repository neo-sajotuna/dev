package com.sparta.newneoboardbuddy.domain.card.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.exception.CommonOptimisticLockingFailureException;
import com.sparta.newneoboardbuddy.common.exception.InvalidRequestException;
import com.sparta.newneoboardbuddy.common.exception.NotFoundException;
import com.sparta.newneoboardbuddy.config.HierarchyUtil;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.board.exception.BoardNotFoundException;
import com.sparta.newneoboardbuddy.domain.board.repository.BoardRepository;
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
import com.sparta.newneoboardbuddy.domain.file.exception.NotFoundFileException;
import com.sparta.newneoboardbuddy.domain.file.service.FileService;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.list.repository.BoardListRepository;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
import com.sparta.newneoboardbuddy.domain.member.service.MemberService;
import com.sparta.newneoboardbuddy.domain.user.entity.User;

import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import com.sparta.newneoboardbuddy.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final BoardListRepository boardListRepository;
    private final CardRepository cardRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CardActivityLogRepository cardActivityLogRepository;
    private final FileService fileService;

    private final WorkspaceRepository workspaceRepository;

    private final HierarchyUtil hierarchyUtil;

    @Transactional
    public CardCreateResponse createCard(Long listId, AuthUser authUser, CardCreateRequest request) {
        User user = User.fromUser(authUser);

        // 혹시 모르니 이부분은 안바꾸겠습니다.
        Member member = memberService.memberPermission(authUser, request.getWorkspaceId());
        System.out.println("member = " + member);

        // 카드 추가될 리스트 조회
        BoardList list = boardListRepository.findByIdWithJoinFetchToWorkspace(listId).orElseThrow(() ->
                new InvalidRequestException("list not found"));
        System.out.println("list = " + list);

        // workspace에 해당 List가 속해 있는지 확인
        if (!hierarchyUtil.isListInWorkspace(request.getWorkspaceId(), list)) {
            throw new InvalidRequestException("작성할 List는 Workspace에 속해있지 않습니다.");
        }

        // 워크스페이스 추가
        Workspace workspace = workspaceRepository.findBySpaceIdWithJoinFetchBoard(request.getWorkspaceId(), request.getBoardId()).orElseThrow( () -> new BoardNotFoundException("보드가 없습니다.") );
        System.out.println("workspace = " + workspace);

        Board board = workspace.getBoards().stream()
                .filter(entity -> entity.getBoardId().equals(request.getBoardId())) // 필터 조건
                .findFirst().orElseThrow(() -> new BoardNotFoundException("보드가 없습니다.")); // 첫 번째 매칭된 결과 반환

        // 담당자 멤버 ID 를 받아서 조회
        Member assignedMember = memberRepository.findById(request.getMemberId()).orElseThrow(()-> new NotFoundException("멤버가 없습니다."));

        Card newCard = new Card(
                request.getCardTitle(),
                request.getCardContent(),
                request.getStartedAt(),
                request.getFinishedAt(),
                assignedMember,
                user,
                list,
                board,
                workspace
        );
        Card savedCard = cardRepository.save(newCard);

        // 파일 저장 로직
        if (request.getFile() != null || !request.getFile().isEmpty()) {
            FileUploadDto fileUploadDto = FileUploadDto.builder()
                    .targetId(savedCard.getCardId())
                    .targetTable("card")
                    .targetFile(request.getFile())
                    .build();

            fileService.uploadFile(fileUploadDto);
            // 파일 저장 로직
        }


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
//        Card card = cardRepository.findById(cardId).orElseThrow(()->
//                new NoSuchElementException("카드를 찾을 수 없다."));

        // 카드 추가될 리스트 조회
        Card card = cardRepository.findByIdWithJoinFetchToWorkspace(cardId).orElseThrow(() ->
                new InvalidRequestException("card not found"));

        // workspace에 해당 List가 속해 있는지 확인
        if (!hierarchyUtil.isCardInWorkspace(request.getWorkspaceId(), card)) {
            throw new InvalidRequestException("작성할 Card는 Workspace에 속해있지 않습니다.");
        }

        // 수정 전 현재 상태
        String oldTitle = card.getCardTitle();
        String oldContent = card.getCardContent();

        // 카드 수정
        card.setCardTitle(request.getCardTitle());
        card.setCardContent(request.getCardContent());

        Long workspaceId = card.getWorkspace().getSpaceId();
        Member member = memberService.memberPermission(authUser, workspaceId);

        // 읽기 전용 유저 생성 못하게 예외처리 해야함
        if (member.getMemberRole() == MemberRole.READ_ONLY_MEMBER){
            throw new InvalidRequestException("읽기 전용 멤버는 카드를 수정할 수 없습니다.");
        }

        if(request.getMemberId() != null){
            Member assignedMember = memberRepository.findById(request.getMemberId())
                    .orElseThrow(()-> new NotFoundException("멤버가 없습니다."));
            card.setMember(assignedMember);
        }


        // 낙관적 락 적용
            Card updateCard = cardRepository.save(card);
            logCardActivity(updateCard, Action.UPDATED, "제목: " + oldTitle + " -> " + updateCard.getCardTitle() +
                    ", 내용 : " + oldContent + " -> " + updateCard.getCardContent() +
                    ", 관리 멤버 :" + " -> " + updateCard.getMember().getMemberId());
            return new CardUpdateResponse(updateCard.getCardId(), updateCard.getCardTitle(), updateCard.getCardContent(), updateCard.getMember().getMemberId(), updateCard.getActiveTime());
    }



    // 비관적 락 사용
    public void updateCardWithLock(Long cardId, CardUpdateRequest request) {
        // 비관적 락 사용 시
        Card card = cardRepository.findCardWithLock(cardId);
        card.setCardTitle(request.getCardTitle());
        card.setCardContent(request.getCardContent());

        cardRepository.save(card);
    }

    // 활동 로그 메서드
    private void logCardActivity(Card card, Action action, String details) {
        CardActivityLog activityLog = new CardActivityLog();
        activityLog.setCard(card);
        activityLog.setAction(action);
        activityLog.setDetails(details);
        activityLog.setActiveTime(LocalDateTime.now());

        cardActivityLogRepository.save(activityLog);
    }

    @Transactional(readOnly = true)
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


    public void deleteCard(Long cardId, AuthUser authUser) {
        // 카드 조회
        // 카드 추가될 리스트 조회
        Card card = cardRepository.findByIdWithJoinFetchToWorkspace(cardId).orElseThrow(() ->
                new InvalidRequestException("card not found"));

        Long workspaceId = card.getWorkspace().getSpaceId();

        Member member = memberService.memberPermission(authUser, workspaceId);

        // workspace에 해당 List가 속해 있는지 확인
        if (!hierarchyUtil.isCardInWorkspace(workspaceId, card)) {
            throw new InvalidRequestException("작성할 Card는 Workspace에 속해있지 않습니다.");
        }

        // 읽기 전용 유저가 카드 삭제 시도시 예외 처리
        if(member.getMemberRole() == MemberRole.READ_ONLY_MEMBER){
            throw new InvalidRequestException("읽기 전용 멤버는 카드를 삭제할 수 없습니다.");
        }

        cardRepository.delete(card);
    }

    public Page<Card> searchCards(Long assignedMemberId, Pageable pageable) {
        return cardRepository.searchCards(assignedMemberId, pageable);

    }
}
