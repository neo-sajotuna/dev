package com.sparta.newneoboardbuddy.domain.card.redis;


import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.service.RedisService;
import com.sparta.newneoboardbuddy.config.RedisCacheResetTask;
import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardRequest;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.board.repository.BoardRepository;
import com.sparta.newneoboardbuddy.domain.card.dto.response.CardDetailResponse;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
import com.sparta.newneoboardbuddy.domain.card.service.CardService;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.entity.CardActivityLog;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.repository.CardActivityLogRepository;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.list.repository.BoardListRepository;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import com.sparta.newneoboardbuddy.domain.user.repository.UserRepository;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import com.sparta.newneoboardbuddy.domain.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class redisTest2 {

    @MockBean
    private BoardListRepository boardListRepository;
    @MockBean
    private CardRepository cardRepository;
    @MockBean
    private WorkspaceRepository workspaceRepository;
    @MockBean
    private BoardRepository boardRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CardActivityLogRepository cardActivityLogRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    RedisCacheResetTask redisCacheResetTask; // 자동 주입

    @MockBean
    private ZSetOperations<String, Object> zSetOperations;

    @MockBean
    private ValueOperations<String, Integer> valueOperations;

    @MockBean
    private RedisService redisService; // 자동 주입

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this); // Mock 객체 초기화

    }


    @Test
    void reids_캐싱을사용x() {

        long startTime = System.nanoTime(); // 시작 시간 기록

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

        BoardRequest boardRequest = new BoardRequest(workspace.getSpaceId(), "으아아", "아아");
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

        // 테스트용 카드와 사용자 객체 생성
        Card card = new Card("제목", "내용", LocalTime.now(), LocalTime.now(), member, user, list, board, workspace);
        card.setCardId(3L);
        // 카드 댓글 세팅
//        card.setComment(new Comment("댓글 내용", authUser.getId(), card));

//        AuthUser authUser = new AuthUser(1L, "duduio2050@gmail.com", UserRole.ROLE_ADMIN);

        CardActivityLog activityLog = new CardActivityLog();
        activityLog.setCard(card);
        activityLog.setActiveTime(LocalTime.now());
        activityLog.setDetails("유저 바뀜");



        // given
        when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        when(cardActivityLogRepository.findTopByCardOrderByActiveTimeDesc(card)).thenReturn(Optional.of(activityLog));
        doNothing().when(redisService).incrementCardView("card", card.getCardId(), authUser.getId());
        // when
        CardDetailResponse response = cardService.getCardDetails(authUser, card.getCardId());


        long endTime = System.nanoTime(); // 종료 시간 기록
        long duration = endTime - startTime; // 실행 시간 계산

        System.out.println("Test duration: " + duration + " nanoseconds");
    }


    @Test
    void reids_캐싱을사용o() {

        long startTime = System.nanoTime(); // 시작 시간 기록

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

        BoardRequest boardRequest = new BoardRequest(workspace.getSpaceId(), "으아아", "아아");
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

        // 테스트용 카드와 사용자 객체 생성
        Card card = new Card("제목", "내용", LocalTime.now(), LocalTime.now(), member, user, list, board, workspace);
        card.setCardId(3L);
        // 카드 댓글 세팅
//        card.setComment(new Comment("댓글 내용", authUser.getId(), card));

//        AuthUser authUser = new AuthUser(1L, "duduio2050@gmail.com", UserRole.ROLE_ADMIN);

        CardActivityLog activityLog = new CardActivityLog();
        activityLog.setCard(card);
        activityLog.setActiveTime(LocalTime.now());
        activityLog.setDetails("유저 바뀜");



        // given
        when(cardRepository.findById(card.getCardId())).thenReturn(Optional.of(card));
        when(cardActivityLogRepository.findTopByCardOrderByActiveTimeDesc(card)).thenReturn(Optional.of(activityLog));

        // when
        CardDetailResponse response = cardService.getCardDetails(authUser, card.getCardId());


        long endTime = System.nanoTime(); // 종료 시간 기록
        long duration = endTime - startTime; // 실행 시간 계산

        System.out.println("Test duration: " + duration + " nanoseconds");

    }

}
