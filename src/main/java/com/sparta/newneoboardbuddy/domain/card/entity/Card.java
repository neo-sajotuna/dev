package com.sparta.newneoboardbuddy.domain.card.entity;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.comment.entity.Comment;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Card")
@NoArgsConstructor
public class Card {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="card_id")
    private Long cardId;

    @Column(name= "card_title")
    private String cardTitle;

    @Column(name="card_content")
    private String cardContent;

    @Column(name="finished_at")
    private LocalTime finishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardList_id", nullable = false)
    private BoardList boardList;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "board", nullable = false)
    private Board board;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Card(String cardTitle, String cardContent, LocalTime finishedAt, User user, BoardList list) {
        this.cardTitle = cardTitle;
        this.cardContent = cardContent;
        this.finishedAt = finishedAt;
//        this.member = member;
        this.user = user;
        this.boardList = list;


    }
}
