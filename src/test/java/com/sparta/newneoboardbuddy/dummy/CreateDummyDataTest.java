package com.sparta.newneoboardbuddy.dummy;

import com.sparta.newneoboardbuddy.config.SlackNotificationUtil;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import com.sparta.newneoboardbuddy.dummy.config.DataHub;
import com.sparta.newneoboardbuddy.dummy.config.DummyDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CreateDummyDataTest {
    @Autowired
    SlackNotificationUtil slackNotificationUtil;

    @Autowired
    DummyDataFactory dummyDataFactory;

    @Autowired
    DataHub datahub;

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
        int cardSize = 10;

        List<User> users = dummyDataFactory.createDummyUser(userSize);
        List<Member> members = new ArrayList<Member>();
        List<Workspace> workspaces = dummyDataFactory.createDummyWorkspaces(workspaceSize, users, members);
        members = dummyDataFactory.saveMembers(members);
        List<Board> boards = dummyDataFactory.createDummyBoard(boardSize, workspaces);
        List<BoardList> boardLists = dummyDataFactory.createDummyBoardList(boardListSize, boards);
        boardLists = dummyDataFactory.getBoardListsFetchJoin();
        List<Card> cards = dummyDataFactory.createDummyCard(cardSize, members, workspaces, boardLists);
    }

    @Test
    void createDummyUsers() {
        int userSize = 10;
        List<User> users = dummyDataFactory.createDummyUser(userSize);
    }

    @Test
    void createDummyWorkspaces() {
        int workspaceSize = 10;
        List<User> users = datahub.getAllUsers();
        List<Member> members = new ArrayList<Member>();

        List<Workspace> workspaces = dummyDataFactory.createDummyWorkspaces(workspaceSize, users, members);
        dummyDataFactory.saveMembers(members);
    }

    void createDummyBoards() {
        int boardSize = 10;
        List<Workspace> workspaces = datahub.getAllWorkspaces();
        List<Board> boards = dummyDataFactory.createDummyBoard(boardSize, workspaces);
    }

    void createBoardList() {
        int boardListSize = 10;

        List<Board> boards = datahub.getAllBoards();
        List<BoardList> boardLists = dummyDataFactory.createDummyBoardList(boardListSize, boards);
    }

    @Test
    void createDummyCards() {
        int cardSize = 250000;
        List<Workspace> workspaces = datahub.getAllWorkspaces();
        List<Board> boards = datahub.getAllBoards();
        List<BoardList> boardLists = dummyDataFactory.getBoardListsFetchJoin();
        List<Member> members = datahub.getAllMembers();

        List<Card> cards = dummyDataFactory.createDummyCard(cardSize, members, workspaces, boardLists);
    }

}
