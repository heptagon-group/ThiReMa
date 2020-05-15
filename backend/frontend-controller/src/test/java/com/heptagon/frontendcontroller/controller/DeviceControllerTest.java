package com.heptagon.frontendcontroller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heptagon.frontendcontroller.controller.dto.DeviceDto;
import com.heptagon.frontendcontroller.controller.exception.AlreadyExistsException;
import com.heptagon.frontendcontroller.controller.exception.DeviceNotFoundException;
import com.heptagon.frontendcontroller.controller.util.RequestUtil;
import com.heptagon.frontendcontroller.controller.util.WithMockCustomUser;
import com.heptagon.frontendcontroller.error.validation.FieldError;
import com.heptagon.frontendcontroller.repository.UserRepository;
import com.heptagon.frontendcontroller.service.DeviceService;
import com.heptagon.thirema.commons.domain.Device;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.heptagon.frontendcontroller.controller.util.DeviceUtil.*;
import static com.heptagon.frontendcontroller.controller.util.MessageUtil.*;
import static com.heptagon.frontendcontroller.controller.util.ResponseBodyMatchers.responseBody;
import static com.heptagon.frontendcontroller.controller.util.ValidationUtil.createFieldsErrors;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockCustomUser
class DeviceControllerTest {

    private static final String PATH = DeviceController.class.getAnnotation(RequestMapping.class).value()[0];

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @MockBean
    private DeviceService deviceService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void getDeviceList_okAndReturnDeviceList() throws Exception {
        List<Device> deviceList = Arrays.asList(createDevice(1L), createDevice(2L));

        when(deviceService.getDeviceList(anyLong())).thenReturn(deviceList);

        mvc.perform(get(PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(deviceList.stream()
                        .map(DeviceDto::fromDevice)
                        .collect(Collectors.toList()))));
    }

    @Test
    public void addDevice_validValues_createdAndReturnDeviceId() throws Exception {
        Device saved = new Device();
        saved.setId(1L);
        when(deviceService.addDevice(any(Device.class))).thenReturn(saved);

        mvc.perform(RequestUtil.requestBody(post(PATH), objectMapper.writeValueAsString(createDevice()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(DeviceDto.fromDevice(saved))));
    }

    @Test
    public void addDevice_nullValues_badRequestAndErrors() throws Exception {
        assertNullValues(DeviceDto.fromDevice(createNullDevice()), true);
    }

    @Test
    public void addDevice_emptyValues_badRequestAndErrors() throws Exception {
        assertBlankValues(DeviceDto.fromDevice(createEmptyDevice()));
    }

    @Test
    public void addDevice_blankValues_badRequestAndErrors() throws Exception {
        assertBlankValues(DeviceDto.fromDevice(createBlankDevice()));
    }

    @Test
    public void addDevice_deviceAlreadyExists_conflictAndError() throws Exception {
        doThrow(new AlreadyExistsException(Collections.singletonList("ipAddress"))).when(deviceService)
                .addDevice(any(Device.class));

        assertDeviceAlreadyExists(post(PATH));
    }

    @Test
    public void updateDevice_deviceExistsAndValidValues_ok() throws Exception {
        assertUpdateIsOk(DeviceDto.fromDevice(createDevice()));
    }

    @Test
    public void updateDevice_deviceExistsAndNullValues_ok() throws Exception {
        assertUpdateIsOk(DeviceDto.fromDevice(createNullDevice()));
    }

    @Test
    public void updateDevice_deviceDoesNotExist_notFound() throws Exception {
        doThrow(new DeviceNotFoundException()).when(deviceService).updateDevice(any(Device.class));

        mvc.perform(RequestUtil.requestBody(patch(PATH + "/{id}", 1L),
                objectMapper.writeValueAsString(DeviceDto.fromDevice(createDevice()))))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(DEVICE_NOT_FOUND));
    }

    @Test
    public void updateDevice_emptyValues_badRequest() throws Exception {
        assertNullOrBlankValues(DeviceDto.fromDevice(createEmptyDevice()));
    }

    @Test
    public void updateDevice_blankValues_badRequest() throws Exception {
        assertNullOrBlankValues(DeviceDto.fromDevice(createBlankDevice()));
    }

    @Test
    public void updateDevice_deviceAlreadyExists_conflictAndError() throws Exception {
        doThrow(new AlreadyExistsException(Collections.singletonList("ipAddress"))).when(deviceService)
                .updateDevice(any(Device.class));

        assertDeviceAlreadyExists(patch(PATH + "/{id}", 1L));
    }

    @Test
    public void deleteDevice_deviceExists_ok() throws Exception {
        mvc.perform(delete(PATH + "/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteDevice_deviceDoesNotExist_notFound() throws Exception {
        doThrow(new DeviceNotFoundException()).when(deviceService)
                .deleteDevice(anyLong(), anyLong());

        mvc.perform(delete(PATH + "/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(DEVICE_NOT_FOUND));
    }

    private void assertUpdateIsOk(DeviceDto device) throws Exception {
        mvc.perform(RequestUtil.requestBody(patch(PATH + "/{id}", 1L),
                objectMapper.writeValueAsString(device)))
                .andExpect(status().isOk());
    }

    private void assertDeviceAlreadyExists(MockHttpServletRequestBuilder request) throws Exception {
        mvc.perform(RequestUtil.requestBody(request, objectMapper.writeValueAsString(
                DeviceDto.fromDevice(createDevice(2L)))))
                .andExpect(status().isConflict())
                .andExpect(responseBody().containsFieldError("ipAddress", ALREADY_EXISTS));
    }

    private void assertBadValues(MockHttpServletRequestBuilder request, DeviceDto device, String message)
            throws Exception {
        mvc.perform(RequestUtil.requestBody(request, objectMapper.writeValueAsString(device)))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsFieldsErrors(
                        Arrays.asList("name", "ipAddress", "brand", "model"), message));
    }

    private void assertBadValues(DeviceDto device, List<FieldError> fieldErrors) throws Exception {
        mvc.perform(RequestUtil.requestBody(post(PATH), objectMapper.writeValueAsString(device)))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsFieldsErrors(fieldErrors));
    }

    private void assertNullValues(DeviceDto device, boolean checkFrequencyAndGatewayId) throws  Exception {
        List<FieldError> fieldErrors = createFieldsErrors(new String[]{"name", "ipAddress", "brand", "model"}
                , NOT_BLANK);
        if (checkFrequencyAndGatewayId) {
            fieldErrors.add(new FieldError("frequency", NOT_NULL));
            fieldErrors.add(new FieldError("gatewayId", NOT_NULL));
        }
        assertBadValues(device, fieldErrors);
    }

    private void assertBlankValues(DeviceDto device) throws Exception {
        assertBadValues(post(PATH), device, NOT_BLANK);
    }

    private void assertNullOrBlankValues(DeviceDto device) throws Exception {
        assertBadValues(patch(PATH + "/{id}", 1L), device, NULL_OR_NOT_BLANK);
    }
}