package com.heptagon.frontendcontroller.controller;

import com.heptagon.frontendcontroller.controller.dto.UserDto;
import com.heptagon.frontendcontroller.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUserList() {
        return userService.getUserList().stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@Validated(UserDto.ValidationAdd.class) @RequestBody UserDto user) {
        userService.addUser(user.toUser());
    }

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable long id,
                           @Validated(UserDto.ValidationUpdate.class) @RequestBody UserDto user) {
        userService.updateUser(user.toUser(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

}
