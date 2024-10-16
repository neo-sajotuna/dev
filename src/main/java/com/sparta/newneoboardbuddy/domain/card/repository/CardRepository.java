package com.sparta.newneoboardbuddy.domain.card.repository;
import java.util.Optional;
import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {

    Optional<Card> findByCardId(Long cardId);

    /**
     * cardId를 기준으로 검색하되,
     * workspace ~ card까지 join fetch 한 후 반환하는 메서드
     * @param cardId 찾고자 하는 cardId
     * @return Optional로 감싸진 cardId를 갖고 있는 Card객체
     */
    @Query("select c from Card c " +
            "join fetch c.boardList bl " +
            "join fetch bl.board b " +
            "join fetch b.workspace  " +
            "where c.cardId = :cardId")
    Optional<Card> findByIdWithJoinFetchToWorkspace(Long cardId);
}
