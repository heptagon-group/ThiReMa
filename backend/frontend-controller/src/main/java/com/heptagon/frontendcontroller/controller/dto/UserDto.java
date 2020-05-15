package com.heptagon.frontendcontroller.controller.dto;

import com.heptagon.thirema.commons.domain.User;
import com.heptagon.thirema.commons.validation.constraints.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    public interface ValidationAdd {}
    public interface ValidationUpdate {}

    private Long id;

    @NotBlank(groups = ValidationAdd.class)
    @NullOrNotBlank(groups = ValidationUpdate.class)
    private String username;

    @NotBlank(groups = ValidationAdd.class)
    @NullOrNotBlank(groups = ValidationUpdate.class)
    private String email;

    @NotBlank(groups = ValidationAdd.class)
    @NullOrNotBlank(groups = ValidationUpdate.class)
    private String telegramUsername;

    @NotBlank(groups = ValidationAdd.class)
    @NullOrNotBlank(groups = ValidationUpdate.class)
    private String password;

    @NullOrNotBlank(groups = {ValidationAdd.class, ValidationUpdate.class})
    private String vatNumber;

    public User toUser(Long id) {
        return User.builder()
                .id(id)
                .username(getUsername())
                .email(getEmail())
                .telegramUsername(getTelegramUsername())
                .password(getPassword())
                .vatNumber(getVatNumber())
                .build();
    }

    public User toUser() {
        return toUser(null);
    }

    public static UserDto fromUser(User user) {
        return builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .telegramUsername(user.getTelegramUsername())
                .password(user.getPassword())
                .vatNumber(user.getVatNumber())
                .build();
    }
}
