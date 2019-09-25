package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.dto.DeveloperDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Developer;
import com.ra.janus.developersteam.service.DeveloperService;
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

class DeveloperControllerMockTest {
    private final DeveloperService developerServiceMock = mock(DeveloperService.class);
    private final ResponseService responseServiceMock = mock(ResponseService.class);
    private final DeveloperController developerController = new DeveloperController(developerServiceMock, responseServiceMock);
    private final ObjectMapper mapper = new ObjectMapper();
    private final long testDeveloperId = 1L;
    private final Developer testDeveloper = new Developer(testDeveloperId, "John");
    private final DeveloperDTO testDeveloperDTO = new DeveloperDTO(testDeveloper);
    private final List<DeveloperDTO> testListDTO = Arrays.asList(testDeveloperDTO);
    private final int responseEntityFieldCount = 3;
    private final String testDeveloperName = testDeveloper.getName();
    private final ResponseEntity<ResponseListDTO<DeveloperDTO>> successListEntity;
    private final ResponseEntity<ResponseDTO<DeveloperDTO>> successOkEntity, successCreatedEntity, validationErrorEntity, persistenceErrorEntity;
    private MockMvc mockMvc;

    public DeveloperControllerMockTest() {
        ResponseListDTO<DeveloperDTO> testResponseListDTO = new ResponseListDTO<>(ResponseService.SUCCESSFUL, null, testListDTO);
        successListEntity = new ResponseEntity<>(testResponseListDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(List.class), eq(HttpStatus.OK))).thenReturn(successListEntity);

        ResponseDTO<DeveloperDTO> testResponseDTO = new ResponseDTO<>(ResponseService.SUCCESSFUL, null, testDeveloperDTO);
        successOkEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(DeveloperDTO.class), eq(HttpStatus.OK))).thenReturn(successOkEntity);
        doReturn(successOkEntity).when(responseServiceMock).success(anyString(), eq(HttpStatus.OK));

        successCreatedEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.CREATED);
        when(responseServiceMock.success(anyString(), any(DeveloperDTO.class), eq(HttpStatus.CREATED))).thenReturn(successCreatedEntity);

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
                .standaloneSetup(developerController)
                .build();
    }

    @Test
    void whenGetDevelopersShouldReturnDevelopersJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.response.length()").value(testListDTO.size()))
                .andExpect(jsonPath("$.response[0].id").value(testDeveloper.getId()));
    }

    @Test
    void whenGetDeveloperShouldReturnDeveloperJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/developers/" + testDeveloperId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testDeveloper.getId()));
    }

    @Test
    void whenCreateADeveloperShouldReturnDeveloperJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                post("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testDeveloperDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testDeveloper.getId()));
    }

    @Test
    void whenCreateADeveloperShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testDeveloperDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                post("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testDeveloperDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testDeveloperDTO.setName(testDeveloperName);

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenUpdateDeveloperShouldReturnDeveloperJsonRepresentation() throws Exception {
        //given
        when(developerServiceMock.update(testDeveloperDTO)).thenReturn(true);

        String testName = "Jill";
        testDeveloperDTO.setName(testName);

        //when
        final ResultActions result = mockMvc.perform(
                put("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testDeveloperDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testDeveloperDTO.setName(testDeveloperName);

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.name").value(testName));
    }

    @Test
    void whenUpdateDeveloperShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(developerServiceMock.update(testDeveloperDTO)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                put("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testDeveloperDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

    @Test
    void whenUpdateDeveloperShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testDeveloperDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                put("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testDeveloperDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testDeveloperDTO.setName(testDeveloperName);
        System.out.println(result.andReturn().getResponse().getContentAsString());

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenDeleteDeveloperShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        when(developerServiceMock.delete(testDeveloperId)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/developers/" + testDeveloperId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenDeleteDeveloperShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(developerServiceMock.delete(testDeveloperId)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/developers/" + testDeveloperId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }
}