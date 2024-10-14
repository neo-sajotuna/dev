package com.sparta.newneoboardbuddy.domain.list.entity;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "list")
public class BoardList {
    @Id
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public BoardList(String title) {
        this.title = title;
    }
}
