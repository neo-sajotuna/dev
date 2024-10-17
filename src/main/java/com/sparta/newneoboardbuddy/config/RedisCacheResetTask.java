package com.sparta.newneoboardbuddy.config;

import com.sparta.newneoboardbuddy.common.service.RedisService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RedisCacheResetTask {

    private final RedisService redisService;

    public RedisCacheResetTask(RedisService redisService) {
        this.redisService = redisService;
    }

    private List<String> useTables = new ArrayList<>();

    @PostConstruct
    public void init() {
        useTables.add("card");
        useTables.add("cardRank");
    }

    // 매일 자정에 캐시 초기화
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetCache() {

        // 카드 조회수 초기화
        redisService.clearZSet(useTables);
        System.out.println("조회수 캐시 리셋 완료");


        // 어뷰징 초기화
        redisService.clearUser();
    }
}