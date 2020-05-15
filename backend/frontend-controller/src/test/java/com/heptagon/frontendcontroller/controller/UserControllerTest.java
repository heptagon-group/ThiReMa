package com.heptagon.frontendcontroller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heptagon.frontendcontroller.controller.dto.UserDto;
import com.heptagon.frontendcontroller.controller.exception.AlreadyExistsException;
import com.heptagon.frontendcontroller.controller.exception.UserNotFoundException;
import com.heptagon.frontendcontroller.repository.UserRepository;
import com.heptagon.frontendcontroller.service.UserService;
import com.heptagon.thirema.commons.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.heptagon.frontendcontroller.controller.util.MessageUtil.*;
import static com.heptagon.frontendcontroller.controller.util.RequestUtil.requestBody;
import static com.heptagon.frontendcontroller.controller.util.ResponseBodyMatchers.responseBody;
import static com.heptagon.frontendcontroller.controller.util.UserUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    private static final String PATH = UserController.class.getAnnotation(RequestMapping.class).value()[0];

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void getUserList_okAndReturnUserList() throws Exception {
        List<User> userList = Arrays.asList(createUser(1L), createUser(2L));

        when(userService.getUserList()).thenReturn(userList);

        mvc.perform(get(PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userList.stream()
                        .map(UserDto::fromUser)
                        .collect(Collectors.toList()))));
    }

    @Test
    public void addUser_validUser_created() throws Exception {
        mvc.perform(requestBody(post(PATH), objectMapper.writeValueAsString(UserDto.fromUser(createUser()))))
                .andExpect(status().isCreated());

    }

    @Test
    public void addUser_nullValues_badRequestAndErrors() throws Exception {
        assertBlankValues(UserDto.fromUser(createNullUser()));
    }

    @Test
    public void addUser_emptyValues_badRequestAndErrors() throws Exception {
        assertBlankValues(UserDto.fromUser(createEmptyUser()));
    }

    @Test
    public void addUser_blankValues_badRequestAndErrors() throws Exception {
        assertBlankValues(UserDto.fromUser(createBlankUser()));
    }

    @Test
    public void addUser_userAlreadyExists_conflictAndError() throws Exception {
        doThrow(new AlreadyExistsException(Arrays.asList("username", "email", "telegramUsername")))
                .when(userService).addUser(any(User.class));

        assertUserAlreadyExists(post(PATH));
    }

    @Test
    public void updateUser_validUser_ok() throws Exception {
        assertUpdateIsOk(UserDto.fromUser(createUser()));
    }

    @Test
    public void updateUser_nullValues_ok() throws Exception {
        assertUpdateIsOk(UserDto.fromUser(createNullUser()));
    }

    @Test
    public void updateUser_emptyValues_badRequestAndErrors() throws Exception {
        assertNullOrBlankValues(UserDto.fromUser(createEmptyUser()));
    }

    @Test
    public void updateUser_blankValues_badRequestAndErrors() throws Exception {
        assertNullOrBlankValues(UserDto.fromUser(createBlankUser()));
    }

    @Test
    public void updateUser_userDoesNotExist_notFound() throws Exception {
        doThrow(new UserNotFoundException()).when(userService).updateUser(any(User.class));

        mvc.perform(requestBody(patch(PATH + "/{id}", 1L),
                objectMapper.writeValueAsString(UserDto.fromUser(createUser()))))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(USER_NOT_FOUND));
    }

    @Test
    public void updateUser_userAlreadyExists_conflictAndError() throws Exception {
        doThrow(new AlreadyExistsException(Arrays.asList("username", "email", "telegramUsername")))
                .when(userService).updateUser(any(User.class));

        assertUserAlreadyExists(patch(PATH + "/{id}", 1L));
    }

    @Test
    public void deleteUser_validUser_ok() throws Exception {
        mvc.perform(delete(PATH + "/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser_userDoesNotExist_notFound() throws Exception {
        doThrow(new UserNotFoundException()).when(userService).deleteUser(anyLong());

        mvc.perform(delete(PATH + "/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(USER_NOT_FOUND));
    }

    private void assertUpdateIsOk(UserDto user) throws Exception {
        mvc.perform(requestBody(patch(PATH + "/{id}", 1L),
                objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    private void assertUserAlreadyExists(MockHttpServletRequestBuilder request) throws Exception {
        mvc.perform(requestBody(request, objectMapper.writeValueAsString(UserDto.fromUser(createUser(2L)))))
                .andExpect(status().isConflict())
                .andExpect(responseBody().containsFieldsErrors(
                        Arrays.asList("username", "email", "telegramUsername"), ALREADY_EXISTS));
    }

    private void assertBadValues(MockHttpServletRequestBuilder request, UserDto user, String message) throws Exception {
        mvc.perform(requestBody(request, objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsFieldsErrors(
                        Arrays.asList("username", "email", "telegramUsername", "password"), message));
    }

    private void assertBlankValues(UserDto user) throws Exception {
        assertBadValues(post(PATH), user, NOT_BLANK);
    }

    private void assertNullOrBlankValues(UserDto user) throws Exception {
        assertBadValues(patch(PATH + "/{id}", 1L), user, NULL_OR_NOT_BLANK);
    }
}