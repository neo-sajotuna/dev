package com.sparta.newneoboardbuddy.domain.card.redis;


import com.sparta.newneoboardbuddy.common.service.RedisService;
import com.sparta.newneoboardbuddy.config.RedisCacheResetTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class redisTest {

    @MockBean
    RedisService redisService; // 자동 주입

    @Autowired
    RedisCacheResetTask redisCacheResetTask; // 자동 주입

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
    }

    @Test
    void 스케쥴러_테스트(){


        redisCacheResetTask.init(); // @PostConstruct 메서드 강제 호출

        // 초기화 진행
        redisCacheResetTask.resetCache();
    }


}
