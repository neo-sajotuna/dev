package com.sparta.newneoboardbuddy.domain.card.entity;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.cardActivityLog.entity.CardActivityLog;
import com.sparta.newneoboardbuddy.domain.comment.entity.Comment;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="Card")
@NoArgsConstructor

public class Card {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="card_id")
    private Long cardId;

    @Setter
    @Column(name= "card_title")
    private String cardTitle;

    @Setter
    @Column(name="card_content")
    private String cardContent;

    @Column(name="started_at")
    private LocalTime startedAt;

    @Column(name="finished_at")
    private LocalTime finishedAt;

    @Column(name = "active_time")
    private LocalDateTime activeTime;

    @Version
    private Integer version; // 낙관적 락 버전 필드

    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardList_id", nullable = false)
    private BoardList boardList;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    public Card(String cardTitle, String cardContent, LocalTime startedAt, LocalTime finishedAt, Member member, User user, BoardList list, Board board, Workspace workspace) {
        this.cardTitle = cardTitle;
        this.cardContent = cardContent;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.member = member;
        this.user = user;
        this.boardList = list;
        this.board = board;
        this.workspace = workspace;
    }

}
