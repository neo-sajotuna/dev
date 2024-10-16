package com.sparta.newneoboardbuddy;

import com.sparta.newneoboardbuddy.config.SlackNotificationUtil;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import com.sparta.newneoboardbuddy.dummy.CreateDummyDataFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class NewNeoBoardBuddyApplicationTests {

    @Autowired
    SlackNotificationUtil slackNotificationUtil;

    @Autowired
    CreateDummyDataFactory createDummyData;
    @Autowired
    private CardRepository cardRepository;

    @Test
    void contextLoads() throws IOException {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 0L);
        ReflectionTestUtils.setField(user, "email", "test@a.com");
        ReflectionTestUtils.setField(user, "userRole", UserRole.ROLE_ADMIN);

        slackNotificationUtil.sendNewUser(user);
    }

    @Test
    void createDummySet() {
        int userSize = 10;
        int workspaceSize = 10;
        int boardSize = 10;
        int boardListSize = 10;
        int cardSize = 500000;

        List<User> users = createDummyData.createDummyUser(userSize);
        List<Member> members = new ArrayList<Member>();
        List<Workspace> workspaces = createDummyData.createDummyWorkspaces(workspaceSize, users, members);
        members = createDummyData.saveMembers(members);
        List<Board> boards = createDummyData.createDummyBoard(boardSize, workspaces);
        List<BoardList> boardLists = createDummyData.createDummyBoardList(boardListSize, boards);
        boardLists = createDummyData.getBoardListsFetchJoin();
        List<Card> cards = createDummyData.createDummyCard(cardSize, members, workspaces, boardLists);
    }

    @Test
    void findCardOwner() {
        String findTitle = "Carrion Comfort";
        List<Card> cards = cardRepository.findByCardTitle(findTitle);

        for (Card card : cards) {
            System.out.println(card);
        }
    }
}
