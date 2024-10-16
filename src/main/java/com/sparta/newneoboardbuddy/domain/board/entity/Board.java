package com.sparta.newneoboardbuddy.domain.board.entity;

import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardRequest;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    private String boardTitle;

    private String background;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardList> boardLists = new ArrayList<>();

    public Board(BoardRequest boardRequest, Workspace workspace) {
        this.boardTitle = boardRequest.getBoardTitle();
        this.background = boardRequest.getBackground();
        this.workspace = workspace;
    }

    public void updateBoard(String boardTitle, String background) {
        this.boardTitle = boardTitle;
        this.background = background;

    }
}
