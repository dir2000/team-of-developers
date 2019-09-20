package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.dto.WorkDTO;
import com.ra.janus.developersteam.entity.Work;
import com.ra.janus.developersteam.service.ResponseService;
import com.ra.janus.developersteam.service.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WorkControllerMockTest {
    private final WorkService workServiceMock = mock(WorkService.class);
    private final ResponseService responseServiceMock = mock(ResponseService.class);
    private final WorkController workController = new WorkController(workServiceMock, responseServiceMock);
    private final ObjectMapper mapper = new ObjectMapper();
    private final long testWorkId = 1L;
    private final Work testWork = new Work(testWorkId, "Testing", BigDecimal.valueOf(100L));
    private final WorkDTO testWorkDTO = new WorkDTO(testWork);
    private final List<WorkDTO> testListDTO = Arrays.asList(testWorkDTO);
    private final int responseEntityFieldCount = 3;
    private final String testWorkName = testWork.getName();
    private final ResponseEntity<ResponseListDTO<WorkDTO>> successListEntity;
    private final ResponseEntity<ResponseDTO<WorkDTO>> successOkEntity, successCreatedEntity, validationErrorEntity, persistenceErrorEntity;
    private MockMvc mockMvc;

    public WorkControllerMockTest() {
        ResponseListDTO<WorkDTO> testResponseListDTO = new ResponseListDTO<>(ResponseService.SUCCESSFUL, null, testListDTO);
        successListEntity = new ResponseEntity<>(testResponseListDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(List.class), eq(HttpStatus.OK))).thenReturn(successListEntity);

        ResponseDTO<WorkDTO> testResponseDTO = new ResponseDTO<>(ResponseService.SUCCESSFUL, null, testWorkDTO);
        successOkEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(WorkDTO.class), eq(HttpStatus.OK))).thenReturn(successOkEntity);
        doReturn(successOkEntity).when(responseServiceMock).success(anyString(), eq(HttpStatus.OK));

        successCreatedEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.CREATED);
        when(responseServiceMock.success(anyString(), any(WorkDTO.class), eq(HttpStatus.CREATED))).thenReturn(successCreatedEntity);

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
                .standaloneSetup(workController)
                .build();
    }

    @Test
    void whenGetWorksShouldReturnWorksJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.response.length()").value(testListDTO.size()))
                .andExpect(jsonPath("$.response[0].id").value(testWork.getId()));
    }

    @Test
    void whenGetWorkShouldReturnWorkJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/works/" + testWorkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testWork.getId()));
    }

    @Test
    void whenCreateAWorkShouldReturnWorkJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                post("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testWorkDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testWork.getId()));
    }

    @Test
    void whenCreateAWorkShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testWorkDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                post("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testWorkDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testWorkDTO.setName(testWorkName);

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenUpdateWorkShouldReturnWorkJsonRepresentation() throws Exception {
        //given
        when(workServiceMock.update(testWorkDTO)).thenReturn(true);

        String testName = "Programming";
        testWorkDTO.setName(testName);

        //when
        final ResultActions result = mockMvc.perform(
                put("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testWorkDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testWorkDTO.setName(testWorkName);

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.name").value(testName));
    }

    @Test
    void whenUpdateWorkShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(workServiceMock.update(testWorkDTO)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                put("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testWorkDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

    @Test
    void whenUpdateWorkShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testWorkDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                put("/works")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testWorkDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testWorkDTO.setName(testWorkName);

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenDeleteWorkShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        when(workServiceMock.delete(testWorkId)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/works/" + testWorkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenDeleteWorkShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(workServiceMock.delete(testWorkId)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/works/" + testWorkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }


    @Test
    void whenPatchWorkShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        BigDecimal newPrice = BigDecimal.valueOf(5L);
        when(workServiceMock.patchPrice(testWorkId, newPrice)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/works/" + testWorkId + "/price/" + newPrice)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenPatchWorkShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        BigDecimal newPrice = BigDecimal.valueOf(5L);
        when(workServiceMock.patchPrice(testWorkId, newPrice)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/works/" + testWorkId + "/price/" + newPrice)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

}