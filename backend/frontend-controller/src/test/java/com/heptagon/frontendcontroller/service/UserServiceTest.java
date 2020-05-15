package com.heptagon.frontendcontroller.service;

import com.heptagon.frontendcontroller.controller.exception.AlreadyExistsException;
import com.heptagon.frontendcontroller.controller.exception.UserNotFoundException;
import com.heptagon.frontendcontroller.controller.util.WithMockCustomUser;
import com.heptagon.frontendcontroller.kafka.event.EventPublisher;
import com.heptagon.frontendcontroller.repository.UserRepository;
import com.heptagon.thirema.commons.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.heptagon.frontendcontroller.controller.util.MessageUtil.ALREADY_EXISTS;
import static com.heptagon.frontendcontroller.controller.util.MessageUtil.USER_NOT_FOUND;
import static com.heptagon.frontendcontroller.controller.util.UserUtil.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserService.class})
@WithMockCustomUser
class UserServiceTest {

    @Inject
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    public void getUserList_returnUserList() {
        List<User> userList = Arrays.asList(createUser(1L), createUser(2L));
        when(userRepository.findAll()).thenReturn(userList);

        List<User> listUserTest = userService.getUserList();

        assertEquals(listUserTest, userList);
    }

    @Test
    public void addUser_ok() {
        User user = createUser(1L);

        userService.addUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void addUser_userAlreadyExists_throwAlreadyExistsException() {
        User user = createUser(1L);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(userRepository.existsByTelegramUsername(anyString())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.addUser(user), ALREADY_EXISTS);
    }

    @Test
    public void updateUser_userExists_ok() {
        User user = createUser(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.updateUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void updateUser_userAlreadyExists_throwAlreadyExistsException() {
        User user = createUser(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(userRepository.existsByTelegramUsername(anyString())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.updateUser(createUser(2L)),
                ALREADY_EXISTS);
    }

    @Test
    public void updateUser_userDoesNotExist_throwUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(createUser(2L)),
                USER_NOT_FOUND);
    }

    @Test
    public void deleteUser_userExists_ok() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteUser_userDoesNotExist_throwUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(anyLong()), USER_NOT_FOUND);
    }
}