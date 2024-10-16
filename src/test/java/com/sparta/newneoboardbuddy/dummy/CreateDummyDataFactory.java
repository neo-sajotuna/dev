package com.sparta.newneoboardbuddy.dummy;

import com.github.javafaker.Faker;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.board.repository.BoardRepository;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.list.repository.BoardListRepository;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.user.repository.UserRepository;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import com.sparta.newneoboardbuddy.domain.workspace.repository.WorkspaceRepository;
import com.sparta.newneoboardbuddy.dummy.board.DummyBoardFactory;
import com.sparta.newneoboardbuddy.dummy.boardlist.DummyBoardlistFactory;
import com.sparta.newneoboardbuddy.dummy.card.DummyCardFactory;
import com.sparta.newneoboardbuddy.dummy.user.DummyUserFactory;
import com.sparta.newneoboardbuddy.dummy.workspace.DummyWorkspaceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CreateDummyDataFactory {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private BoardListRepository boardListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private DummyUserFactory dummyUserFactory;
    @Autowired
    private DummyWorkspaceFactory dummyWorkspaceFactory;
    @Autowired
    private DummyBoardFactory dummyBoardFactory;
    @Autowired
    private DummyBoardlistFactory dummyBoardlistFactory;
    @Autowired
    private DummyCardFactory dummyCardFactory;

    private final Faker faker = new Faker();
    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public List<User> createDummyUser(int size) {
        List<User> users = dummyUserFactory.createDummyUser(faker, size);

        return userRepository.saveAll(users);
    }

    @Transactional
    public List<Workspace> createDummyWorkspaces(int size, List<User> users, List<Member> members) {
        List<Workspace> workspaces = dummyWorkspaceFactory.createDummyWorkspace(faker, size, users, members);

        return workspaceRepository.saveAll(workspaces);
    }

    @Transactional
    public List<Member> saveMembers(List<Member> members) {

        return memberRepository.saveAll(members);
    }

    @Transactional
    public List<Board> createDummyBoard(int size, List<Workspace> workspaces) {
        List<Board> boards = dummyBoardFactory.createDummyBoards(faker, size, workspaces);

        return boardRepository.saveAll(boards);
    }

    @Transactional
    public List<BoardList> createDummyBoardList(int size, List<Board> boards) {
        List<BoardList> boardLists = dummyBoardlistFactory.createDummyBoardlist(faker, size, boards);

        return boardListRepository.saveAll(boardLists);
    }

    @Transactional(readOnly = true)
    public List<BoardList> getBoardListsFetchJoin() {
        return boardListRepository.findAllJoinFetchToWorkspace();
    }

    @Transactional
    public List<Card> createDummyCard(int size, List<Member> members, List<Workspace> workspaces, List<BoardList> boardlists) {
        List<Card> cards = dummyCardFactory.createDummyCard(faker, size, members, workspaces, boardlists);

        return cardRepository.saveAll(cards);
    }
}
