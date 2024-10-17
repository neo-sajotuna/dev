package com.sparta.newneoboardbuddy;

import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.card.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class NewNeoBoardBuddyApplicationTests {

    @Autowired
    CardRepository cardRepository;

    @Test
    void contextLoads() throws IOException {

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
    }
}
