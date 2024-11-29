package com.sparta.newneoboardbuddy.domain.card.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.exception.InvalidRequestException;
import com.sparta.newneoboardbuddy.common.exception.NotFoundException;
import com.sparta.newneoboardbuddy.common.service.RedisService;
import com.sparta.newneoboardbuddy.config.HierarchyUtil;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.board.exception.BoardNotFoundException;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardCreateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardUpdateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardCreateResponse;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardDetailResponse;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardUpdateResponse;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.entity.CardActivityLog;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.enums.Action;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.logResponse.LogResponseDto;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.repository.CardActivityLogRepository;
import com.sparta.newneoboardbuddy.domain.comment.dto.response.CommentSaveResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

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
    private final RedisService redisService;

    private final HierarchyUtil hierarchyUtil;

    /**
     * 카드를 생성하는 메서드
     * @param listId Card를 생성할 List Id
     * @param request Card생성에 필요한 정보가 담긴 Request
     * @param authUser Filter에서 인증이 완료된 유저 정보
     * @return 생성된 Card 정보가 담긴 Dto
     */
    @Transactional
    public CardCreateResponse createCard(Long listId, AuthUser authUser, CardCreateRequest request) {
        User user = User.fromUser(authUser);
        Member member = memberService.verifyMember(authUser, request.getWorkspaceId());
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

    /**
     * 카드 정보를 수정하는 메서드
     * @param cardId 수정할 Card Id
     * @param authUser Filter에서 인증이 완료된 유저 정보
     * @param request 카드 수정에 필요한 Request 정보
     * @return 수정된 카드 정보가 담긴 Dto객체
     */
    public CardUpdateResponse updateCard(Long cardId, AuthUser authUser, CardUpdateRequest request) {
        // 카드 추가될 리스트  (낙관적 락)
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
        Member member = memberService.verifyMember(authUser, workspaceId);

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

        LocalTime latestActiveTime = cardActivityLogRepository.findFirstByCard_CardIdOrderByActiveTimeDesc(updateCard.getCardId())
                .map(CardActivityLog::getActiveTime) // CardActivityLog에서 activeTime 값만 추출
                .orElseThrow(()-> new NotFoundException("활동시간 없다."));

        return new CardUpdateResponse(updateCard.getCardId(), updateCard.getCardTitle(), updateCard.getCardContent(), updateCard.getMember().getMemberId(), latestActiveTime);
    }


    /**
     * 낙관적 Lock 해당 Card에 참조한 횟수를 증가 시켜주는 메서드 ( Test Code에서 사용 )
     * @param cardId 조회할 카드 Id
     */
    @Transactional
    public void incrementCount(Long cardId){
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("카드 없다"));
        card.addTestCount();
    }

    /**
     * 해당 카드에 변경 로그를 남기는 메서드
     * @param card 로그를 남길 Card 객체
     * @param action 카드가 변경된 상황 ( 생성, 변경 )
     * @param details 상세 설명 / 생성 : title & 변경 : before title -> after title
     */
    private void logCardActivity(Card card, Action action, String details) {
        CardActivityLog activityLog = new CardActivityLog();
        activityLog.setCard(card);
        activityLog.setAction(action);
        activityLog.setDetails(details);
        activityLog.setActiveTime(LocalTime.now());

        cardActivityLogRepository.save(activityLog);
    }

    /**
     * 해당 카드의 변경 로그까지 반환하는 메서드
     * @param cardId 조회 & 반환할 CardId
     * @return 변경 로그까지 담긴 CardDetail Dto
     */
    @Transactional(readOnly = true)
    public CardDetailResponse getCardDetails(AuthUser authUser, Long cardId) {

//        System.out.println(1);
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new NotFoundException("카드가 없다."));

        CardActivityLog activityLog = cardActivityLogRepository.findTopByCardOrderByActiveTimeDesc(card)
                .orElseThrow(()-> new NotFoundException("카드 활동 내역 없다"));

        // 카드 활동 내역 조회
        LogResponseDto activityLogDto = new LogResponseDto(activityLog);

        // 카드 조회 시 조회카운트
        redisService.incrementCardView("card", cardId, authUser.getId());

        // 카드 댓글 조회
        List<CommentSaveResponseDto> commentsDto = card.getComments().stream()
                .map(comment -> new CommentSaveResponseDto(
                        comment.getCommentId(),
                        comment.getComment(),
                        comment.getEmoji(),
                        comment.getCreatedAt()))
                .toList();

        System.out.println(1);

        return new CardDetailResponse(
                card.getCardId(),
                card.getCardTitle(),
                card.getCardContent(),
                card.getStartedAt(),
                card.getFinishedAt(),
                activityLogDto,
                commentsDto
        );
    }

    /**
     * 카드를 삭제하는 메서드
     * @param cardId 삭제할 Card Id
     * @param authUser Filter에서 인증이 완료된 유저 정보
     */
    public void deleteCard(Long cardId, AuthUser authUser) {
        // 카드 조회
        // 카드 추가될 리스트 조회
        Card card = cardRepository.findByIdWithJoinFetchToWorkspace(cardId).orElseThrow(() ->
                new InvalidRequestException("card not found"));

        Long workspaceId = card.getWorkspace().getSpaceId();

        Member member = memberService.verifyMember(authUser, workspaceId);

        // workspace에 해당 List가 속해 있는지 확인
        if (!hierarchyUtil.isCardInWorkspace(workspaceId, card)) {
            throw new InvalidRequestException("작성할 Card는 Workspace에 속해있지 않습니다.");
        }

        cardRepository.delete(card);
    }

    /**
     * Null이 아닌 조건 내용 모두 포함하고 있는 카드들을 페이징하여 반환하는 메서드
     * @param cardTitle Card.title에 cardTitle의 내용이 들어 있는지 확인할 문자열
     * @param cardContent Card.content에 cardContent의 내용이 들어 있는지 확인할 문자열
     * @param assignedMemberId Card.userId에 assignedMemberId와 일치 여부를 확인할 Card와 동일한 Space에 소속인 UserId
     * @param boardId Card.board.boardId와 일치 여부를 확인할 boardId
     * @param pageable 페이징 조건
     * @return 위 내용 중 하나라도 만족하는 페이징 된 Card객체
     */
    public Page<CardCreateResponse> searchCards(String cardTitle, String cardContent, Long assignedMemberId, Long boardId, Pageable pageable) {
        return new PageImpl<>(cardRepository.searchCards(cardTitle, cardContent, assignedMemberId, boardId, pageable).stream().map(CardCreateResponse::new).toList());
    }
}
