package com.sparta.newneoboardbuddy.domain.comment.service;


import com.sparta.newneoboardbuddy.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

}
