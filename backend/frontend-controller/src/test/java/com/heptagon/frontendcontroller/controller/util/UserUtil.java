package com.heptagon.frontendcontroller.controller.util;

import com.heptagon.thirema.commons.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUtil {

    public static User createUser(String value) {
        return User.builder()
                .id(1L)
                .username(value)
                .email(value)
                .telegramUsername(value)
                .telegramUserId(null)
                .password(value)
                .vatNumber(value)
                .build();
    }

    public static User createUser(Long id) {
        return User.builder()
                .id(id)
                .username("username" + id)
                .email("email" + id)
                .telegramUsername("telegramUsername" +  id)
                .telegramUserId(null)
                .password("password" + id)
                .vatNumber("vatNumber" + id)
                .build();
    }

    public static User createUser() {
        return createUser(1L);
    }

    public static User createNullUser() {
        return createUser((String) null);
    }

    public static User createEmptyUser() {
        return createUser("");
    }

    public static User createBlankUser() {
        return createUser(" ");
    }
}
