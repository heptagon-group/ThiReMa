package com.heptagon.frontendcontroller.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heptagon.thirema.commons.domain.User;
import com.heptagon.frontendcontroller.error.validation.FieldError;
import com.heptagon.frontendcontroller.repository.UserRepository;
import com.heptagon.frontendcontroller.security.controller.dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.heptagon.frontendcontroller.controller.util.MessageUtil.*;
import static com.heptagon.frontendcontroller.controller.util.RequestUtil.requestBody;
import static com.heptagon.frontendcontroller.controller.util.ResponseBodyMatchers.responseBody;
import static com.heptagon.frontendcontroller.controller.util.UserUtil.createUser;
import static com.heptagon.frontendcontroller.controller.util.ValidationUtil.createFieldsErrors;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    private static final String PATH = AuthenticationController.class.getAnnotation(RequestMapping.class).value()[0];

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository repository;

    @Test
    public void authenticate_validLogin_okAndReturnToken() throws Exception {
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(encodePassword(createUser())));

        mvc.perform(requestBody(post(PATH), objectMapper.writeValueAsString(createLogin(createUser())))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void authenticate_validValuesAndUserDoesNotExist_unauthorizedAndError() throws Exception {
        assertBadCredentials();
    }

    @Test
    public void authenticate_validValuesAndWrongPassword_unauthorizedAndError() throws Exception {
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(encodePassword(createUser())));

        assertBadCredentials();
    }

    @Test
    public void authenticate_nullValues_badRequestAndErrors() throws Exception {
        assertBadValues(createNullLogin(), true);
    }

    @Test
    public void authenticate_emptyValues_badRequestAndErrors() throws Exception {
        assertBadValues(createEmptyLogin());
    }

    @Test
    public void authenticate_blankValues_badRequestAndErrors() throws Exception {
        assertBadValues(createBlankLogin());
    }

    private User encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    private void assertBadCredentials() throws Exception {
        mvc.perform(requestBody(post(PATH), objectMapper.writeValueAsString(createLogin()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason(BAD_CREDENTIALS));
    }

    private void assertBadValues(LoginDto login, List<FieldError> fieldErrors) throws Exception {
        mvc.perform(requestBody(post(PATH), objectMapper.writeValueAsString(login))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsFieldsErrors(fieldErrors));
    }

    private void assertBadValues(LoginDto login, boolean checkRememberMe) throws Exception {
        List<FieldError> fieldErrors = createFieldsErrors(new String[]{"username", "password"}, NOT_BLANK);
        if (checkRememberMe) {
            fieldErrors.add(new FieldError("rememberMe", NOT_NULL));
        }
        assertBadValues(login, fieldErrors);
    }

    private void assertBadValues(LoginDto login) throws Exception {
        assertBadValues(login, false);
    }

    private static LoginDto createLogin(String username, String password, Boolean rememberMe) {
        return new LoginDto(username, password, rememberMe);
    }

    private static LoginDto createLogin(String username, String password) {
        return createLogin(username, password, true);
    }

    private static LoginDto createNullLogin() {
        return createLogin(null, null, null);
    }

    private static LoginDto createEmptyLogin() {
        return createLogin("", "");
    }

    private static LoginDto createBlankLogin() {
        return createLogin(" ", " ");
    }

    private static LoginDto createLogin(User user) {
        return createLogin(user.getUsername(), user.getPassword());
    }

    private static LoginDto createLogin() {
        return createLogin("username", "password");
    }
}