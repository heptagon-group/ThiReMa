package com.heptagon.frontendcontroller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heptagon.frontendcontroller.controller.dto.MeasureDto;
import com.heptagon.frontendcontroller.controller.exception.AlreadyExistsException;
import com.heptagon.frontendcontroller.controller.exception.DeviceNotFoundException;
import com.heptagon.frontendcontroller.controller.exception.MeasureNotFoundException;
import com.heptagon.frontendcontroller.controller.util.WithMockCustomUser;
import com.heptagon.frontendcontroller.error.validation.FieldError;
import com.heptagon.frontendcontroller.repository.UserRepository;
import com.heptagon.frontendcontroller.service.MeasureService;
import com.heptagon.thirema.commons.domain.Measure;
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

import static com.heptagon.frontendcontroller.controller.util.MeasureUtil.*;
import static com.heptagon.frontendcontroller.controller.util.MessageUtil.*;
import static com.heptagon.frontendcontroller.controller.util.RequestUtil.requestBody;
import static com.heptagon.frontendcontroller.controller.util.ResponseBodyMatchers.responseBody;
import static com.heptagon.frontendcontroller.controller.util.ValidationUtil.createFieldsErrors;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeasureController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockCustomUser
class MeasureControllerTest {

    private static final String PATH = MeasureController.class.getAnnotation(RequestMapping.class).value()[0];

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @MockBean
    private MeasureService measureService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void getMeasureList_deviceExists_okAndReturnMeasureList() throws Exception {
        List<Measure> measureList = Arrays.asList(createMeasure(1L), createMeasure(2L));

        when(measureService.getMeasureList(anyLong(), anyLong())).thenReturn(measureList);

        mvc.perform(get(PATH ,1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(measureList.stream()
                        .map(MeasureDto::fromMeasure)
                        .collect(Collectors.toList()))));
    }

    @Test
    public void getMeasureList_deviceDoesNotExist_notFound() throws Exception {
        doThrow(new DeviceNotFoundException()).when(measureService).getMeasureList(anyLong(), anyLong());

        mvc.perform(get(PATH,1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(DEVICE_NOT_FOUND));
    }

    @Test
    public void addMeasure_deviceExists_MeasureCreated() throws Exception {
        mvc.perform(requestBody(post(PATH,1L),
                objectMapper.writeValueAsString(MeasureDto.fromMeasure(createMeasure()))))
                .andExpect(status().isCreated());
    }

    @Test
    public void addMeasure_deviceDoesNotExists_NotFound() throws Exception {
        doThrow(new DeviceNotFoundException()).when(measureService).addMeasure(any(Measure.class));

        mvc.perform(requestBody(post(PATH,1L),
                objectMapper.writeValueAsString(MeasureDto.fromMeasure(createMeasure()))))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(DEVICE_NOT_FOUND));
    }

    @Test
    public void addMeasure_measureAlreadyExists_conflictAndError() throws Exception {
        doThrow(new AlreadyExistsException(Collections.singletonList("name"))).when(measureService)
                .addMeasure(any(Measure.class));

        assertMeasureAlreadyExists(post(PATH, 1L));
    }

    @Test
    public void updateMeasure_deviceAndMeasureExists_ok() throws Exception {
        assertUpdateIsOk(MeasureDto.fromMeasure(createMeasure()));
    }

    @Test
    public void updateMeasure_DeviceNotExist_notFound() throws Exception {
        doThrow(new DeviceNotFoundException()).when(measureService)
                .updateMeasure(any(Measure.class));

        assertUpdateIsNotFound(DEVICE_NOT_FOUND);
    }

    @Test
    public void updateMeasure_measureDoesNotExist_notFound() throws Exception {
        doThrow(new MeasureNotFoundException()).when(measureService)
                .updateMeasure(any(Measure.class));

        assertUpdateIsNotFound(MEASURE_NOT_FOUND);
    }

    @Test
    public void addMeasure_nullValues_badRequestAndErrors() throws Exception {
        assertNullValues(MeasureDto.fromMeasure(createNullMeasure()), true);
    }

    @Test
    public void addMeasure_emptyValues_badRequestAndErrors() throws Exception {
        assertBlankValues(MeasureDto.fromMeasure(createEmptyMeasure()));
    }

    @Test
    public void addMeasure_blankValues_badRequestAndErrors() throws Exception {
        assertBlankValues(MeasureDto.fromMeasure(createBlankMeasure()));
    }

    @Test
    public void updateMeasure_emptyMeasure_badRequest() throws Exception {
        assertNullOrBlankValues(MeasureDto.fromMeasure(createEmptyMeasure()));
    }

    @Test
    public void updateMeasure_blankMeasure_badRequest() throws Exception {
        assertNullOrBlankValues(MeasureDto.fromMeasure(createBlankMeasure()));
    }

    @Test
    public void updateMeasure_measureAlreadyExist_conflictAndError() throws Exception {
        doThrow(new AlreadyExistsException(Collections.singletonList("name"))).when(measureService)
                .updateMeasure(any(Measure.class));

        assertMeasureAlreadyExists(patch(PATH + "/{id}", 1L, 1L));
    }

    @Test
    public void deleteMeasure_DeviceExist_ok() throws Exception {
        mvc.perform(delete(PATH + "/{id}", 1L, 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMeasure_deviceDoesNotExist_notFound() throws Exception {
        doThrow(new DeviceNotFoundException()).when(measureService)
                .deleteMeasure(anyLong(), anyLong(), anyLong());

        mvc.perform(delete(PATH + "/{id}", 1L, 1L))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(DEVICE_NOT_FOUND));
    }

    @Test
    public void deleteMeasure_deviceExistsAndMeasureDoesNotExist_notFound() throws Exception {
        doThrow(new MeasureNotFoundException()).when(measureService)
                .deleteMeasure(anyLong(), anyLong(), anyLong());

        mvc.perform(delete(PATH + "/{id}", 1L, 1L))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(MEASURE_NOT_FOUND));
    }

    private void assertUpdateIsOk(MeasureDto measure) throws Exception {
        mvc.perform(requestBody(patch(PATH + "/{id}" ,1L, 1L),
                objectMapper.writeValueAsString(measure)))
                .andExpect(status().isOk());
    }

    private void assertUpdateIsNotFound(String error) throws Exception {
        mvc.perform(requestBody(patch(PATH + "/{id}" ,1L, 1L),
                objectMapper.writeValueAsString(MeasureDto.fromMeasure(createMeasure()))))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(error));
    }

    private void assertMeasureAlreadyExists(MockHttpServletRequestBuilder request) throws Exception {
        mvc.perform(requestBody(request, objectMapper.writeValueAsString(MeasureDto.fromMeasure(createMeasure(2L)))))
                .andExpect(status().isConflict())
                .andExpect(responseBody().containsFieldError(
                        "name", ALREADY_EXISTS));
    }

    private void assertBadValues(MockHttpServletRequestBuilder request, MeasureDto measure, String message)
            throws Exception {
        mvc.perform(requestBody(request, objectMapper.writeValueAsString(measure)))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsFieldsErrors(
                        Arrays.asList("name", "format"), message));
    }

    private void assertBadValues(MeasureDto measure, List<FieldError> fieldErrors) throws Exception {
        mvc.perform(requestBody(post(PATH, 1L), objectMapper.writeValueAsString(measure)))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsFieldsErrors(fieldErrors));
    }

    private void assertNullValues(MeasureDto measure, boolean checkThresholdAndInfluential) throws Exception {
        List<FieldError> fieldErrors = createFieldsErrors(new String[]{"name", "format"}, NOT_BLANK);
        if (checkThresholdAndInfluential){
            fieldErrors.add(new FieldError("threshold", NOT_NULL));
            fieldErrors.add(new FieldError("thresholdGreater", NOT_NULL));
            fieldErrors.add(new FieldError("influential", NOT_NULL));
        }
        assertBadValues(measure, fieldErrors);
    }

    private void assertBlankValues(MeasureDto measure) throws Exception {
        assertBadValues(post(PATH, 1L), measure, NOT_BLANK);
    }

    private void assertNullOrBlankValues(MeasureDto measure) throws Exception {
        assertBadValues(patch(PATH + "/{id}", 1L, 1L), measure, NULL_OR_NOT_BLANK);
    }
}