package com.sparta.newneoboardbuddy.dummy.card;

import com.github.javafaker.Faker;
import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardRequest;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DummyCardFactory {

    public List<Card> createDummyCard(Faker faker, int size, List<Member> members, List<Workspace> workspaces, List<BoardList> boardLists) {
        List<Card> cards = new ArrayList<Card>();
        List<List<Member>> distributeMembers = distributeMembersByWorkspace(members, workspaces);

        for (int i = 0; i < size; i++) {
            int next = faker.random().nextInt(boardLists.size());
            BoardList boardList = boardLists.get(next);
            Board board = boardList.getBoard();
            Workspace workspace = board.getWorkspace();

            int memberSize = distributeMembers.get((int)(long)workspace.getSpaceId()).size();
            int memberNext = 0;
            if (memberSize > 1) {
                memberNext = faker.random().nextInt(memberSize - 1) + 1;
            }

            List<Member> workspaceMember = distributeMembers.get((int)(long)workspace.getSpaceId());

            Card card = new Card(
                    faker.book().title(),
                    faker.book().author(),
                    LocalTime.now(),
                    LocalTime.now(),
                    workspaceMember.get(memberNext),
                    workspaceMember.get(memberNext).getUser(),
                    boardList,
                    board,
                    workspace
                    );

            cards.add(card);
        }

        return cards;
    }

    private List<List<Member>> distributeMembersByWorkspace(List<Member> members, List<Workspace> workspaces) {
        List<List<Member>> result = new ArrayList<>();

        for (int i = 0; i < workspaces.size() + 1; i++) {
            result.add(new ArrayList<>());
        }

        for(Member member : members) {
            int index = (int)((long)member.getWorkspace().getSpaceId());

            result.get(index).add(member);
        }

        return result;
    }
}
