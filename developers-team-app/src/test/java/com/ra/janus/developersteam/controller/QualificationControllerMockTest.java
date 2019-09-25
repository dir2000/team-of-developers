package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.dto.QualificationDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Qualification;
import com.ra.janus.developersteam.service.QualificationService;
import com.ra.janus.developersteam.service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class QualificationControllerMockTest {
    private final QualificationService qualificationServiceMock = mock(QualificationService.class);
    private final ResponseService responseServiceMock = mock(ResponseService.class);
    private final QualificationController qualificationController = new QualificationController(qualificationServiceMock, responseServiceMock);
    private final ObjectMapper mapper = new ObjectMapper();
    private final long testQualificationId = 1L;
    private final Qualification testQualification = new Qualification(testQualificationId, "Some", "Little");
    private final QualificationDTO testQualificationDTO = new QualificationDTO(testQualification);
    private final List<QualificationDTO> testListDTO = Arrays.asList(testQualificationDTO);
    private final int responseEntityFieldCount = 3;
    private final String testQualificationName = testQualification.getName();
    private final ResponseEntity<ResponseListDTO<QualificationDTO>> successListEntity;
    private final ResponseEntity<ResponseDTO<QualificationDTO>> successOkEntity, successCreatedEntity, validationErrorEntity, persistenceErrorEntity;
    private MockMvc mockMvc;

    public QualificationControllerMockTest() {
        ResponseListDTO<QualificationDTO> testResponseListDTO = new ResponseListDTO<>(ResponseService.SUCCESSFUL, null, testListDTO);
        successListEntity = new ResponseEntity<>(testResponseListDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(List.class), eq(HttpStatus.OK))).thenReturn(successListEntity);

        ResponseDTO<QualificationDTO> testResponseDTO = new ResponseDTO<>(ResponseService.SUCCESSFUL, null, testQualificationDTO);
        successOkEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(QualificationDTO.class), eq(HttpStatus.OK))).thenReturn(successOkEntity);
        doReturn(successOkEntity).when(responseServiceMock).success(anyString(), eq(HttpStatus.OK));

        successCreatedEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.CREATED);
        when(responseServiceMock.success(anyString(), any(QualificationDTO.class), eq(HttpStatus.CREATED))).thenReturn(successCreatedEntity);

        testResponseDTO = new ResponseDTO<>(ResponseService.VALIDATION_ERROR, null, null);
        validationErrorEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.BAD_REQUEST);
        doReturn(validationErrorEntity).when(responseServiceMock).validationError(anyString());

        testResponseDTO = new ResponseDTO<>(ResponseService.PERSISTENCE_ERROR, null, null);
        persistenceErrorEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        doReturn(persistenceErrorEntity).when(responseServiceMock).persistenceError(anyString());
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(qualificationController)
                .build();
    }

    @Test
    void whenGetQualificationsShouldReturnQualificationsJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/qualifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.response.length()").value(testListDTO.size()))
                .andExpect(jsonPath("$.response[0].id").value(testQualification.getId()));
    }

    @Test
    void whenGetQualificationShouldReturnQualificationJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/qualifications/" + testQualificationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testQualification.getId()));
    }

    @Test
    void whenCreateAQualificationShouldReturnQualificationJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                post("/qualifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testQualificationDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testQualification.getId()));
    }

    @Test
    void whenCreateAQualificationShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testQualificationDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                post("/qualifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testQualificationDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testQualificationDTO.setName(testQualificationName);

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenUpdateQualificationShouldReturnQualificationJsonRepresentation() throws Exception {
        //given
        when(qualificationServiceMock.update(testQualificationDTO)).thenReturn(true);

        String testName = "Jill";
        testQualificationDTO.setName(testName);

        //when
        final ResultActions result = mockMvc.perform(
                put("/qualifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testQualificationDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testQualificationDTO.setName(testQualificationName);

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.name").value(testName));
    }

    @Test
    void whenUpdateQualificationShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(qualificationServiceMock.update(testQualificationDTO)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                put("/qualifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testQualificationDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

    @Test
    void whenUpdateQualificationShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testQualificationDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                put("/qualifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testQualificationDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testQualificationDTO.setName(testQualificationName);
        System.out.println(result.andReturn().getResponse().getContentAsString());

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenDeleteQualificationShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        when(qualificationServiceMock.delete(testQualificationId)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/qualifications/" + testQualificationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenDeleteQualificationShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(qualificationServiceMock.delete(testQualificationId)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/qualifications/" + testQualificationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }


    @Test
    void whenPatchQualificationShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        String newResponsibility = "big";
        when(qualificationServiceMock.patchResponsibility(testQualificationId, newResponsibility)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/qualifications/" + testQualificationId + "/responsibility/" + newResponsibility)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenPatchQualificationShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        String newResponsibility = "big";
        when(qualificationServiceMock.patchResponsibility(testQualificationId, newResponsibility)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/qualifications/" + testQualificationId + "/responsibility/" + newResponsibility)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

}