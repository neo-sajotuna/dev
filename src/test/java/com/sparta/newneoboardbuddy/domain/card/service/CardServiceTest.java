package com.sparta.newneoboardbuddy.domain.card.service;

import com.amazonaws.services.kms.model.InvalidAliasNameException;
import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.exception.CommonOptimisticLockingFailureException;
import com.sparta.newneoboardbuddy.common.exception.InvalidRequestException;
import com.sparta.newneoboardbuddy.config.HierarchyUtil;
import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardRequest;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.board.repository.BoardRepository;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardCreateRequest;
import com.sparta.newneoboardbuddy.domain.card.dto.request.CardUpdateRequest;
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
import com.sparta.newneoboardbuddy.domain.user.repository.UserRepository;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import com.sparta.newneoboardbuddy.domain.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;


@SpringBootTest
class CardServiceTest {

    @Autowired
    private CardService cardService;
    @Autowired
    private BoardListRepository boardListRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;

    void setUp(){
        Card card = new Card();
        card.setCardTitle("Old Title");
        card.setCardContent("Old Content");
    }

    @Test
    void createCard() {
//        // given
//        AuthUser authUser = new AuthUser(1L, "gusrnr5153@naver.com", UserRole.ROLE_ADMIN);
//        User user = User.fromUser(authUser);
//        Long workspaceId = 1L;
//        Long listId = 1L;
//
//        Member member = new Member();
//        member.setMemberRole(MemberRole.WORKSPACE_MEMBER);
//        given(memberService.memberPermission(authUser, workspaceId)).willReturn(member);
//
//        BoardList list = new BoardList();
//        given(boardListRepository.findById(listId)).willReturn(Optional.of(list));
//
//        Long assignMemberId = 1L;
//        CardCreateRequest request = new CardCreateRequest(workspaceId, "dkdk", "아아아", LocalTime.now().plusHours(10), LocalTime.now().plusHours(20), assignMemberId);
//
//        Member assignedMember = new Member(); // 실제 카드에 할당될 담당자 멤버 생성
//        assignedMember.setMemberId(assignMemberId); // assignMemberId 설정
//        given(memberRepository.findById(assignMemberId)).willReturn(Optional.of(assignedMember));
//
//        Card card = new Card("dkdk", "아아아", LocalTime.now().plusHours(10), LocalTime.now().plusHours(20), assignedMember, user, list);
//        given(cardRepository.save(any(Card.class))).willReturn(card);
//
//        // when
//        CardCreateResponse response = cardService.createCard(listId, authUser, request);
//
//        // then
//        assertNotNull(response);
//        assertEquals("dkdk", response.getCardTitle());
//        assertEquals(assignMemberId, response.getMemberId()); // 할당된 멤버 ID가 일치하는지 검증
//        verify(cardRepository, times(1)).save(any(Card.class));
    }


    @Test
    void updateCard_낙관적_락_적용() throws InterruptedException {
        // given
        AuthUser authUser = new AuthUser(1L, "gusrnr5153@naver.com", UserRole.ROLE_ADMIN);
        User user = User.fromUser(authUser);
        userRepository.save(user);
        MultipartFile file = new MockMultipartFile(
                "file",
                "testFile.txt",
                "text/plain",
                "This is the file content".getBytes()
        );
        Workspace workspace = new Workspace();
        workspace.setSpaceId(1L);
        workspaceRepository.save(workspace);

        BoardRequest boardRequest = new BoardRequest(workspace.getSpaceId(),"으아아","아아");
        Board board = new Board(boardRequest, workspace);
//        board.setBoardId(1L);
        boardRepository.save(board);

        Member member = new Member(user, workspace, MemberRole.WORKSPACE_MEMBER);
        member.setMemberId(1L);
        memberRepository.save(member);


        BoardList list = new BoardList("list");
        list.setListId(1L);
        list.setBoard(board);
        boardListRepository.save(list);

//        boardListRepository.findByIdWithJoinFetchToWorkspace(list.getListId()).orElseThrow(()->new InvalidRequestException("list not found"));


        CardCreateRequest cardCreateRequest = new CardCreateRequest(list.getBoard().getWorkspace().getSpaceId(), list.getBoard().getBoardId(),"dkdk", "아아아", LocalTime.now().plusHours(10), LocalTime.now().plusHours(20),member.getMemberId(), file);

        CardCreateResponse cardCreateResponse = cardService.createCard(list.getListId(), authUser, cardCreateRequest);
        Long cardId= cardCreateResponse.getCardId();

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger optimisticLockExceptionCount = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            System.out.println(i);
            executorService.submit(() -> {
                try {
                    CardUpdateRequest request = new CardUpdateRequest(workspace.getSpaceId(),"new title","new content", member.getMemberId(), LocalTime.now());
                    cardService.updateCard(cardId, authUser, request); // 낙관적 락을 적용한 메서드 호출
                } catch (OptimisticLockingFailureException e) {
                    // 예외 발생 시 처리
                    optimisticLockExceptionCount.incrementAndGet();
                    System.out.println(optimisticLockExceptionCount.get());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown(); // 모든 스레드 동시에 시작
                }
            });
        }

        latch.await();
        executorService.shutdown();

         // 모든 작업이 완료될 때까지 대기
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        int finalCount = cardRepository.findByCardId(cardId).orElseThrow(()-> new IllegalArgumentException("card not found")).getCount();
        int testedTotalCount = optimisticLockExceptionCount.get() + finalCount;

        assertTrue(optimisticLockExceptionCount.get() > 0);
        System.out.println("낙관적 락 실행 시간: " + duration + "ms");
        System.out.println("발생한 예외 수:" + optimisticLockExceptionCount.get());
        System.out.println("요청 수: " + threadCount + ", " + "테스트한 토탈 카운트: " + testedTotalCount);
        System.out.println(optimisticLockExceptionCount.get());
        System.out.println(finalCount);
    }

    @Test
    @Transactional
    void testPessimisticLockingPerformance() throws InterruptedException {
//        // given
//        Long memberId = 2L;
//        Long cardId = 1L;
//        AuthUser authUser = new AuthUser(1L, "gusrnr5153@naver.com", UserRole.ROLE_ADMIN);
//        CardUpdateRequest request = new CardUpdateRequest("New Title", "New Content", memberId, LocalTime.now());
//
//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        long startTime = System.currentTimeMillis();
//
//        for (int i = 0; i < threadCount; i++) {
//            executorService.submit(() -> {
//                try {
//                    cardService.updateCardWithLock(cardId, request); // 비관적 락을 적용한 메서드 호출
//                    cardService.updateCard(cardId, authUser, request); // 카드를 수정
//                } catch (Exception e) {
//                    // 예외 발생 시 처리
//                    System.out.println("Exception: " + e.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await(10, TimeUnit.SECONDS); // 모든 작업이 완료될 때까지 대기
//        long endTime = System.currentTimeMillis();
//
//        System.out.println("비관적 락 소요 시간: " + (endTime - startTime) + "ms");
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