package com.sparta.newneoboardbuddy;

import com.sparta.newneoboardbuddy.config.SlackNotificationUtil;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

@SpringBootTest
class NewNeoBoardBuddyApplicationTests {

    @Autowired
    SlackNotificationUtil slackNotificationUtil;

    @Test
    void contextLoads() throws IOException {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 0L);
        ReflectionTestUtils.setField(user, "email", "test@a.com");
        ReflectionTestUtils.setField(user, "userRole", UserRole.ROLE_ADMIN);

        slackNotificationUtil.sendNewUser(user);
    }

}
