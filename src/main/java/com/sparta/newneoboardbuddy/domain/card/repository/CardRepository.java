package com.sparta.newneoboardbuddy.domain.card.repository;

import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {

}
