package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.dto.DeveloperDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Developer;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
class DeveloperControllerIntegrationTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final DeveloperDTO testDeveloperDTO = new DeveloperDTO(new Developer(1L, "Nick"));
    private MockMvc mockMvc;
    @Autowired
    private DeveloperController developerController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(developerController)
                .build();
    }

    @Test
    void whenGetDeveloperDTOsShouldReturnDeveloperDTOs() throws Exception {
        //given
        ResponseEntity<ResponseListDTO<DeveloperDTO>> listResponseEntity = developerController.getDevelopers();
        for (DeveloperDTO dto : listResponseEntity.getBody().getResponse()) {
            developerController.deleteDeveloper(dto.getId());
        }

        DeveloperDTO created = createDTO(testDeveloperDTO);
        List<DeveloperDTO> expected = Arrays.asList(created);

        //when
        MvcResult result = mockMvc.perform(
                get("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseListDTO<DeveloperDTO> responseListDTO =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<ResponseListDTO<DeveloperDTO>>() {
                        });

        List<DeveloperDTO> actual = responseListDTO.getResponse();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void whenGetDeveloperDTOShouldReturnDeveloperDTO() throws Exception {
        //given
        DeveloperDTO expected = createDTO(testDeveloperDTO);

        //when
        MvcResult result = mockMvc.perform(
                get("/developers/{id}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();
        DeveloperDTO actual = getDTOFromMVCResult(result);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateDeveloperShouldReturnDeveloperDTO() throws Exception {
        //when
        DeveloperDTO created = createDTO(testDeveloperDTO);

        //then
        assertTrue(created instanceof DeveloperDTO);
    }

    @Test
    void whenUpdateDeveloperShouldReturnConfirmation() throws Exception {
        //given
        DeveloperDTO dto = createDTO(testDeveloperDTO);
        dto.setName("Jamshut");

        //when
        MvcResult result = mockMvc.perform(
                put("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<DeveloperDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    @Test
    void whenDeleteDeveloperShouldReturnConfirmation() throws Exception {
        //given
        DeveloperDTO dto = createDTO(testDeveloperDTO);

        //when
        MvcResult result = mockMvc.perform(
                delete("/developers/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<DeveloperDTO> responseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, responseDTO.getResultCode());
    }

    private DeveloperDTO createDTO(DeveloperDTO dtoToCreate) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoToCreate))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE))
                .andReturn();
        return getDTOFromMVCResult(result);
    }

    private DeveloperDTO getDTOFromMVCResult(MvcResult result) throws Exception {
        ResponseDTO<DeveloperDTO> responseDTO = getResponseDTOFromMvcResult(result);
        return responseDTO.getDto();
    }

    private ResponseDTO<DeveloperDTO> getResponseDTOFromMvcResult(MvcResult result) throws java.io.IOException {
        return mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDTO<DeveloperDTO>>() {
                });
    }
}