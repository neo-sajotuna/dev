package com.sparta.newneoboardbuddy.domain.comment.entity;

import com.sparta.newneoboardbuddy.common.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment extends Timestamped {

    @Id
    private Long id;

    @Column(nullable = false)
    private String comment;

}
