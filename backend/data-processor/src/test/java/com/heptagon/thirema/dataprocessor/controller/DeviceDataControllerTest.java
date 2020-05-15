package com.heptagon.thirema.dataprocessor.controller;

import com.heptagon.thirema.dataprocessor.controller.exception.DeviceNotFoundException;
import com.heptagon.thirema.dataprocessor.controller.exception.InfluentialOrNotInfluentialMeasureNotFound;
import com.heptagon.thirema.dataprocessor.controller.exception.MeasureNotFoundException;
import com.heptagon.thirema.dataprocessor.service.DeviceDataService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

import static com.heptagon.thirema.dataprocessor.controller.util.MessageUtil.*;
import static com.heptagon.thirema.dataprocessor.controller.util.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceDataController.class)
@AutoConfigureMockMvc(addFilters = false)
class DeviceDataControllerTest {

    private static final String PATH = DeviceDataController.class.getAnnotation(RequestMapping.class).value()[0];

    @Inject
    private MockMvc mvc;

    @MockBean
    private DeviceDataService deviceDataService;

    @Test
    public void getDeviceData_deviceDoesNotExists_deviceNotFound() throws Exception {
        doThrow(new DeviceNotFoundException()).when(deviceDataService).getDeviceData(anyLong());

        assertNotFoundException(DEVICE_NOT_FOUND);
    }

    @Test
    public void getDeviceData_deviceExists_measureListIsEmpty() throws Exception {
        doThrow(new MeasureNotFoundException()).when(deviceDataService).getDeviceData(anyLong());

        assertNotFoundException(MEASURE_NOT_FOUND);
    }

    @Test
    public void getDeviceData_influentialOrNotInfluentialMeasureNotExists_influentialOrNotInfluentialMeasureNotFound()
        throws Exception {
        doThrow(new InfluentialOrNotInfluentialMeasureNotFound()).when(deviceDataService).getDeviceData(anyLong());

        assertNotFoundException(INFLUENTIAL_OR_NOT_INFLUENTIAL_MEASURE_NOT_FOUND);
    }

    private void assertNotFoundException(String message) throws Exception {
        mvc.perform(get(PATH + "/data", 1L))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().hasError(message));
    }

}