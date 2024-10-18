package com.sparta.newneoboardbuddy;

import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewNeoBoardBuddyApplicationTests {

    @Autowired
    CardRepository cardRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void findCardOwner() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String findTitle = "Carrion Comfort";
        List<Card> cards = cardRepository.findByCardTitle(findTitle);

        for (Card card : cards) {
            System.out.println(card.getCardId());
        }

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

//        assertFalse(cards.isEmpty());
    }
}
