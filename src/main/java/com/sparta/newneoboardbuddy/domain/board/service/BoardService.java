package com.sparta.newneoboardbuddy.domain.board.service;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board getBoardFetchJoinToWorkspace(Long boardId) {
        return boardRepository.findByBoardWithJoinFetchToWorkspace(boardId).orElseThrow(
                ()->new NoSuchElementException("해당 보드가 존재하지 않습니다.")
        );
    }
}
