package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.dto.WorkDTO;
import com.ra.janus.developersteam.entity.Work;
import com.ra.janus.developersteam.service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
class WorkControllerIntegrationTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final WorkDTO testWorkDTO = new WorkDTO(new Work(1L, "Developer", new BigDecimal("4000.00")));
    private MockMvc mockMvc;
    @Autowired
    private WorkController workController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(workController)
                .build();
    }

    @Test
    void whenGetWorkDTOsShouldReturnWorkDTOs() throws Exception {
        //given
        ResponseEntity<ResponseListDTO<WorkDTO>> listResponseEntity = workController.getWorks();
        for (WorkDTO dto : listResponseEntity.getBody().getResponse()) {
            workController.deleteWork(dto.getId());
        }

        WorkDTO created = createDTO(testWorkDTO);
        List<WorkDTO> expected = Arrays.asList(created);

        //when
        MvcResult result = mockMvc.perform(
                get("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseListDTO<WorkDTO> responseListDTO =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<ResponseListDTO<WorkDTO>>() {
                        });

        List<WorkDTO> actual = responseListDTO.getResponse();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void whenGetWorkDTOShouldReturnWorkDTO() throws Exception {
        //given
        WorkDTO expected = createDTO(testWorkDTO);

        //when
        MvcResult result = mockMvc.perform(
                get("/works/{id}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();
        WorkDTO actual = getDTOFromMVCResult(result);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateWorkShouldReturnWorkDTO() throws Exception {
        //when
        WorkDTO created = createDTO(testWorkDTO);

        //then
        assertTrue(created instanceof WorkDTO);
    }

    @Test
    void whenUpdateWorkShouldReturnConfirmation() throws Exception {
        //given
        WorkDTO dto = createDTO(testWorkDTO);
        dto.setName("Digging");

        //when
        MvcResult result = mockMvc.perform(
                put("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<WorkDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    @Test
    void whenDeleteWorkShouldReturnConfirmation() throws Exception {
        //given
        WorkDTO dto = createDTO(testWorkDTO);

        //when
        MvcResult result = mockMvc.perform(
                delete("/works/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<WorkDTO> responseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, responseDTO.getResultCode());
    }

    @Test
    void whenPatchWorkShouldReturnConfirmation() throws Exception {
        //given
        WorkDTO dto = createDTO(testWorkDTO);

        //when
        MvcResult result = mockMvc.perform(
                patch("/works/{id}/price/{newPrice}", dto.getId(), new BigDecimal("3000.00"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<WorkDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    private WorkDTO createDTO(WorkDTO dtoToCreate) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoToCreate))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE))
                .andReturn();
        return getDTOFromMVCResult(result);
    }

    private WorkDTO getDTOFromMVCResult(MvcResult result) throws Exception {
        ResponseDTO<WorkDTO> responseDTO = getResponseDTOFromMvcResult(result);
        return responseDTO.getDto();
    }

    private ResponseDTO<WorkDTO> getResponseDTOFromMvcResult(MvcResult result) throws java.io.IOException {
        return mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDTO<WorkDTO>>() {
                });
    }
}