package com.sparta.newneoboardbuddy.domain.list.repository;

import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardListRepository extends JpaRepository<BoardList, Long> {
}
