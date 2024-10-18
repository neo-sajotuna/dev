//package com.sparta.newneoboardbuddy.domain.card.service;
//
//import com.sparta.newneoboardbuddy.common.dto.AuthUser;
//import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardRequest;
//import com.sparta.newneoboardbuddy.domain.board.entity.Board;
//import com.sparta.newneoboardbuddy.domain.board.repository.BoardRepository;
//import com.sparta.newneoboardbuddy.domain.card.dto.request.CardCreateRequest;
//import com.sparta.newneoboardbuddy.domain.card.dto.response.CardCreateResponse;
//import com.sparta.newneoboardbuddy.domain.card.entity.Card;
//import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
//import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
//import com.sparta.newneoboardbuddy.domain.list.repository.BoardListRepository;
//import com.sparta.newneoboardbuddy.domain.member.entity.Member;
//import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
//import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
//import com.sparta.newneoboardbuddy.domain.user.entity.User;
//import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
//import com.sparta.newneoboardbuddy.domain.user.repository.UserRepository;
//import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
//import com.sparta.newneoboardbuddy.domain.workspace.repository.WorkspaceRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.OptimisticLockingFailureException;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalTime;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//@SpringBootTest
//class CardServiceTest {
//
//    @Autowired
//    private CardService cardService;
//    @Autowired
//    private BoardListRepository boardListRepository;
//    @Autowired
//    private CardRepository cardRepository;
//    @Autowired
//    private WorkspaceRepository workspaceRepository;
//    @Autowired
//    private BoardRepository boardRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    void setUp(){
//        Card card = new Card();
//        card.setCardTitle("Old Title");
//        card.setCardContent("Old Content");
//    }
//
//    @Test
//    void updateCard_낙관적_락_적용() throws InterruptedException {
//        // given
//        AuthUser authUser = new AuthUser(1L, "gusrnr5153@naver.com", UserRole.ROLE_ADMIN);
//        User user = User.fromUser(authUser);
//        userRepository.save(user);
//        MultipartFile file = new MockMultipartFile(
//                "file",
//                "testFile.txt",
//                "text/plain",
//                "This is the file content".getBytes()
//        );
//        Workspace workspace = new Workspace();
//        workspace.setSpaceId(1L);
//        workspaceRepository.save(workspace);
//
//        BoardRequest boardRequest = new BoardRequest(workspace.getSpaceId(),"으아아","아아");
//        Board board = new Board(boardRequest, workspace);
////        board.setBoardId(1L);
//        boardRepository.save(board);
//
//        Member member = new Member(user, workspace, MemberRole.WORKSPACE_MEMBER);
//        member.setMemberId(1L);
//        memberRepository.save(member);
//
//
//        BoardList list = new BoardList("list");
//        list.setListId(1L);
//        list.setBoard(board);
//        boardListRepository.save(list);
//
////        boardListRepository.findByIdWithJoinFetchToWorkspace(list.getListId()).orElseThrow(()->new InvalidRequestException("list not found"));
//
//
//        CardCreateRequest cardCreateRequest = new CardCreateRequest(list.getBoard().getWorkspace().getSpaceId(), list.getBoard().getBoardId(),"dkdk", "아아아", LocalTime.now().plusHours(10), LocalTime.now().plusHours(20),member.getMemberId(), file);
//
//        CardCreateResponse cardCreateResponse = cardService.createCard(list.getListId(), authUser, cardCreateRequest);
//        Long cardId= cardCreateResponse.getCardId();
//
//        int threadCount = 1000;
//        ExecutorService executorService = Executors.newFixedThreadPool(10); // 낙관적 락킹 적용할 스레드풀 설정 -> 동시에 최대 10개의 스레드가 실행 +  스레드가 완료되면 그 자리를 다른 스레드가 사용
//
//
//
//        // 스레드 작업이 완료될 때까지 대기하게 하는 역할 -> 각 스레드가 작업을 완료할때마다 latch.countDown() 메서드 호출하여 카운트 감소시킨다.
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//
//        // 예외 발생 횟수 추적하기 위한 변수
//        AtomicInteger optimisticLockExceptionCount = new AtomicInteger(0);
//
//        long startTime = System.currentTimeMillis();
//
//        for (int i = 0; i < threadCount; i++) {
//            System.out.println(i);
//            executorService.submit(() -> {
//                try {
//                    cardService.incrementCount(cardId);
//                } catch (OptimisticLockingFailureException e) {
//                    // 예외 발생 시 처리
//                    optimisticLockExceptionCount.incrementAndGet();
//                    System.out.println(optimisticLockExceptionCount.get());
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                } finally {
//                    latch.countDown(); // 모든 스레드 동시에 시작
//                }
//            });
//        }
//
//
//        latch.await(); // 호출한 스레드가 threadCount 만큼의 작업이 모두 완료될떄까지 대기 상태로 만든다.
//        executorService.shutdown(); // 스레드가 모두 호출되면 종료
//
//        // 모든 작업이 완료될 때까지 대기
//        long endTime = System.currentTimeMillis();
//        long duration = endTime - startTime;
//        int finalCount = cardRepository.findByCardId(cardId).orElseThrow(()-> new IllegalArgumentException("card not found")).getCount();
//        int testedTotalCount = optimisticLockExceptionCount.get() + finalCount;
//
//        assertTrue(optimisticLockExceptionCount.get() > 0);
//        System.out.println("낙관적 락 실행시간 : " + duration + "ms");
//        System.out.println("실패 횟수 :" + optimisticLockExceptionCount.get());
//        System.out.println("설정한 테스트 수 : " + threadCount + ", " + "전체 테스트 수: " + testedTotalCount);
//        System.out.println("성공적으로 완료된 작업 수: " + finalCount);
//    }
//
//
////    @Test
////    void updatedCard_락_사용_안했을때() throws InterruptedException {
////        // given
////        AuthUser authUser = new AuthUser(1L, "gusrnr5153@naver.com", UserRole.ROLE_ADMIN);
////        User user = User.fromUser(authUser);
////        userRepository.save(user);
////        MultipartFile file = new MockMultipartFile(
////                "file",
////                "testFile.txt",
////                "text/plain",
////                "This is the file content".getBytes()
////        );
////        Workspace workspace = new Workspace();
////        workspace.setSpaceId(1L);
////        workspaceRepository.save(workspace);
////
////        BoardRequest boardRequest = new BoardRequest(workspace.getSpaceId(),"으아아","아아");
////        Board board = new Board(boardRequest, workspace);
////        boardRepository.save(board);
////
////        Member member = new Member(user, workspace, MemberRole.WORKSPACE_MEMBER);
////        member.setMemberId(1L);
////        memberRepository.save(member);
////
////
////        BoardList list = new BoardList("list");
////        list.setListId(1L);
////        list.setBoard(board);
////        boardListRepository.save(list);
////
////        CardCreateRequest cardCreateRequest = new CardCreateRequest(list.getBoard().getWorkspace().getSpaceId(), list.getBoard().getBoardId(),"dkdk", "아아아", LocalTime.now().plusHours(10), LocalTime.now().plusHours(20),member.getMemberId(), file);
////
////        CardCreateResponse cardCreateResponse = cardService.createCard(list.getListId(), authUser, cardCreateRequest);
////        Long cardId= cardCreateResponse.getCardId();
////
////        int threadCount = 1000;
////        ExecutorService executorService = Executors.newFixedThreadPool(10); // 스레드풀 설정 -> 동시에 최대 10개의 스레드가 실행 +  스레드가 완료되면 그 자리를 다른 스레드가 사용
////
////
////
////        // 스레드 작업이 완료될 때까지 대기하게 하는 역할 -> 각 스레드가 작업을 완료할때마다 latch.countDown() 메서드 호출하여 카운트 감소시킨다.
////        CountDownLatch latch = new CountDownLatch(threadCount);
////        long startTime = System.currentTimeMillis();
////
////
////
////        // 예외 발생 횟수 추적하기 위한 변수
////        AtomicInteger successfulCount = new AtomicInteger(0);
////        AtomicInteger failureCount = new AtomicInteger(0);
////
////
////        for (int i = 0; i < threadCount; i++) {
////            System.out.println(i);
////            executorService.submit(() -> {
////                try {
////                    cardService.incrementCount(cardId);
////                    successfulCount.incrementAndGet();
////                }catch (Exception e) {
////                    failureCount.incrementAndGet();
////                } finally {
////                    latch.countDown(); // 모든 스레드 동시에 시작
////                }
////            });
////        }
////
////
////        latch.await(); // 호출한 스레드가 threadCount 만큼의 작업이 모두 완료될떄까지 대기 상태로 만든다.
////        executorService.shutdown(); // 스레드가 모두 호출되면 종료
////
////        int finalCount = cardRepository.findByCardId(cardId).orElseThrow(()-> new IllegalArgumentException("card not found")).getCount();
////        long endTime = System.currentTimeMillis();
////        long duration = endTime - startTime;
////
////
////        System.out.println("실행 시간 : "  + duration + "ms");
////        System.out.println("실패 횟수 : " + failureCount.get());
////        System.out.println("성공한 업데이트 수: " + successfulCount.get());
////
////        // 락을 사용하지 않았기 때문에 동시성 문제가 발생할 수 있으며, 성공한 업데이트 수와 count 값이 다를 수 있음
////        assertNotEquals(successfulCount.get(),finalCount);
////
////    }
//}