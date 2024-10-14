package com.sparta.newneoboardbuddy.domain.comment.entity;

import com.sparta.newneoboardbuddy.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

}
