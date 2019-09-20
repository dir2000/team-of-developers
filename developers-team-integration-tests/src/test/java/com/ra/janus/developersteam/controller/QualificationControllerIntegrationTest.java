package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.dto.QualificationDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Qualification;
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

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
class QualificationControllerIntegrationTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final QualificationDTO testQualificationDTO = new QualificationDTO(new Qualification(1L, "Web Developer", "Front End"));
    private MockMvc mockMvc;
    @Autowired
    private QualificationController qualificationController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(qualificationController)
                .build();
    }

    @Test
    void whenGetQualificationDTOsShouldReturnQualificationDTOs() throws Exception {
        //given
        ResponseEntity<ResponseListDTO<QualificationDTO>> listResponseEntity = qualificationController.getQualifications();
        for (QualificationDTO dto : listResponseEntity.getBody().getResponse()) {
            qualificationController.deleteQualification(dto.getId());
        }

        QualificationDTO created = createDTO(testQualificationDTO);
        List<QualificationDTO> expected = Arrays.asList(created);

        //when
        MvcResult result = mockMvc.perform(
                get("/qualifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseListDTO<QualificationDTO> responseListDTO =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<ResponseListDTO<QualificationDTO>>() {
                        });

        List<QualificationDTO> actual = responseListDTO.getResponse();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void whenGetQualificationDTOShouldReturnQualificationDTO() throws Exception {
        //given
        QualificationDTO expected = createDTO(testQualificationDTO);

        //when
        MvcResult result = mockMvc.perform(
                get("/qualifications/{id}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();
        QualificationDTO actual = getDTOFromMVCResult(result);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateQualificationShouldReturnQualificationDTO() throws Exception {
        //when
        QualificationDTO created = createDTO(testQualificationDTO);

        //then
        assertTrue(created instanceof QualificationDTO);
    }

    @Test
    void whenUpdateQualificationShouldReturnConfirmation() throws Exception {
        //given
        QualificationDTO dto = createDTO(testQualificationDTO);
        dto.setName("Some");

        //when
        MvcResult result = mockMvc.perform(
                put("/qualifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<QualificationDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    @Test
    void whenDeleteQualificationShouldReturnConfirmation() throws Exception {
        //given
        QualificationDTO dto = createDTO(testQualificationDTO);

        //when
        MvcResult result = mockMvc.perform(
                delete("/qualifications/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<QualificationDTO> responseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, responseDTO.getResultCode());
    }

    @Test
    void whenPatchQualificationShouldReturnConfirmation() throws Exception {
        //given
        QualificationDTO dto = createDTO(testQualificationDTO);

        //when
        MvcResult result = mockMvc.perform(
                patch("/qualifications/{id}/responsibility/{newResponsibility}", dto.getId(), "High")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<QualificationDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    private QualificationDTO createDTO(QualificationDTO dtoToCreate) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/qualifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoToCreate))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE))
                .andReturn();
        return getDTOFromMVCResult(result);
    }

    private QualificationDTO getDTOFromMVCResult(MvcResult result) throws Exception {
        ResponseDTO<QualificationDTO> responseDTO = getResponseDTOFromMvcResult(result);
        return responseDTO.getDto();
    }

    private ResponseDTO<QualificationDTO> getResponseDTOFromMvcResult(MvcResult result) throws java.io.IOException {
        return mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDTO<QualificationDTO>>() {
                });
    }
}