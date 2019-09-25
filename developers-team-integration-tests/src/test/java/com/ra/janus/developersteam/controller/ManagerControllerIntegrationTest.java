package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.dto.ManagerDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Manager;
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
class ManagerControllerIntegrationTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ManagerDTO testManagerDTO = new ManagerDTO(new Manager(1L, "John", "manager@gmail.com", "050-000-11-22"));
    private MockMvc mockMvc;
    @Autowired
    private ManagerController managerController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(managerController)
                .build();
    }

    @Test
    void whenGetManagerDTOsShouldReturnManagerDTOs() throws Exception {
        //given
        ResponseEntity<ResponseListDTO<ManagerDTO>> listResponseEntity = managerController.getManagers();
        for (ManagerDTO dto : listResponseEntity.getBody().getResponse()) {
            managerController.deleteManager(dto.getId());
        }

        ManagerDTO created = createDTO(testManagerDTO);
        List<ManagerDTO> expected = Arrays.asList(created);

        //when
        MvcResult result = mockMvc.perform(
                get("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseListDTO<ManagerDTO> responseListDTO =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<ResponseListDTO<ManagerDTO>>() {
                        });

        List<ManagerDTO> actual = responseListDTO.getResponse();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void whenGetManagerDTOShouldReturnManagerDTO() throws Exception {
        //given
        ManagerDTO expected = createDTO(testManagerDTO);

        //when
        MvcResult result = mockMvc.perform(
                get("/managers/{id}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();
        ManagerDTO actual = getDTOFromMVCResult(result);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateManagerShouldReturnManagerDTO() throws Exception {
        //when
        ManagerDTO created = createDTO(testManagerDTO);

        //then
        assertTrue(created instanceof ManagerDTO);
    }

    @Test
    void whenUpdateManagerShouldReturnConfirmation() throws Exception {
        //given
        ManagerDTO dto = createDTO(testManagerDTO);
        dto.setName("Dub");

        //when
        MvcResult result = mockMvc.perform(
                put("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<ManagerDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    @Test
    void whenDeleteManagerShouldReturnConfirmation() throws Exception {
        //given
        ManagerDTO dto = createDTO(testManagerDTO);

        //when
        MvcResult result = mockMvc.perform(
                delete("/managers/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<ManagerDTO> responseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, responseDTO.getResultCode());
    }

    @Test
    void whenPatchManagerShouldReturnConfirmation() throws Exception {
        //given
        ManagerDTO dto = createDTO(testManagerDTO);

        //when
        MvcResult result = mockMvc.perform(
                patch("/managers/{id}/phone/{newPhone}", dto.getId(), "222-33-44")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<ManagerDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    private ManagerDTO createDTO(ManagerDTO dtoToCreate) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoToCreate))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE))
                .andReturn();
        return getDTOFromMVCResult(result);
    }

    private ManagerDTO getDTOFromMVCResult(MvcResult result) throws Exception {
        ResponseDTO<ManagerDTO> responseDTO = getResponseDTOFromMvcResult(result);
        return responseDTO.getDto();
    }

    private ResponseDTO<ManagerDTO> getResponseDTOFromMvcResult(MvcResult result) throws java.io.IOException {
        return mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDTO<ManagerDTO>>() {
                });
    }
}