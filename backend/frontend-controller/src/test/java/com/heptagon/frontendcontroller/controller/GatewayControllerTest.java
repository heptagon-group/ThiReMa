package com.heptagon.frontendcontroller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heptagon.frontendcontroller.controller.dto.GatewayDto;
import com.heptagon.frontendcontroller.controller.exception.GatewayNotFoundException;
import com.heptagon.frontendcontroller.controller.util.WithMockCustomUser;
import com.heptagon.frontendcontroller.repository.UserRepository;
import com.heptagon.frontendcontroller.service.GatewayService;
import com.heptagon.thirema.commons.domain.Gateway;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.heptagon.frontendcontroller.controller.util.GatewayUtil.createGateway;
import static com.heptagon.frontendcontroller.controller.util.MessageUtil.GATEWAY_NOT_FOUND;
import static com.heptagon.frontendcontroller.controller.util.RequestUtil.requestBody;
import static com.heptagon.frontendcontroller.controller.util.ResponseBodyMatchers.responseBody;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GatewayController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockCustomUser
class GatewayControllerTest {

    private static final String PATH = GatewayController.class.getAnnotation(RequestMapping.class).value()[0];

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @MockBean
    private GatewayService gatewayService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void getUserGatewayList_okAndReturnGatewayList() throws Exception {
        List<Gateway> gatewayList = Arrays.asList(createGateway(), createGateway());

        when(gatewayService.getGatewayList(1L)).thenReturn(gatewayList);

        mvc.perform(get(PATH))
                .andExpect(content().json(objectMapper.writeValueAsString(gatewayList.stream()
                        .map(GatewayDto::fromGateway)
                        .collect(Collectors.toList()))));
    }

    @Test
    public void getNewGateway_createdAndReturnGatewayId() throws Exception {
        Gateway created = createGateway();
        when(gatewayService.getNewGatewayId(any(Gateway.class))).thenReturn(created);

        mvc.perform(requestBody(get(PATH + "/new"),
                objectMapper.writeValueAsString(GatewayDto.fromGateway(created))))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteGateway_gatewayExists_ok() throws Exception {
        mvc.perform(delete(PATH + "/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteGateway_gatewayDoesNotExist_notFound() throws Exception {
        doThrow(new GatewayNotFoundException()).when(gatewayService).deleteGateway(anyLong(), anyLong());

        mvc.perform(delete(PATH + "/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(GATEWAY_NOT_FOUND));
    }
}