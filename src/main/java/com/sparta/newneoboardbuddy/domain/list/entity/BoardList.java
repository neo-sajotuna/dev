package com.sparta.newneoboardbuddy.domain.list.entity;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.Join;

import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Setter
@AllArgsConstructor
@Table(name = "boardlist")

public class BoardList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id")
    private Long listId;

    private String title;

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "boardList", cascade = CascadeType.ALL)
    private List<Card> cards;

    public BoardList(String title) {
        this.title = title;
    }
    public void update(String title) { this.title = title; }
    public void swapIndex(Long newIndex) { this.listIndex = newIndex; }
}
