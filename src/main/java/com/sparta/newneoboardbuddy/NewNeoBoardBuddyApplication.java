package com.sparta.newneoboardbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NewNeoBoardBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewNeoBoardBuddyApplication.class, args);


    }
}
