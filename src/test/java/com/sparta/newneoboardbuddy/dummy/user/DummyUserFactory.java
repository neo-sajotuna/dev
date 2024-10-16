package com.sparta.newneoboardbuddy.dummy.user;

import com.github.javafaker.Faker;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyUserFactory {

    /**
     * Random한 User 데이터를 만들어주는 메서드
     * @param size DummyData 개수
     * @return 완성된 User dummyData
     */
    public List<User> createDummyUser(Faker faker, int size) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<User> users = new ArrayList<User>();

        for (int i = 0; i < size; i++) {
            String randomPassword = faker.letterify("?????????");
            String encodedPassword = passwordEncoder.encode(randomPassword);

            users.add(new User(faker.internet().emailAddress(), encodedPassword, UserRole.ROLE_ADMIN));
        }

        return users;
    }
}
