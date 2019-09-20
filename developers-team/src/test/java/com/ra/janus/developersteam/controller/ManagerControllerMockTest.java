package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.dto.ManagerDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Manager;
import com.ra.janus.developersteam.service.ManagerService;
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

class ManagerControllerMockTest {
    private final ManagerService managerServiceMock = mock(ManagerService.class);
    private final ResponseService responseServiceMock = mock(ResponseService.class);
    private final ManagerController managerController = new ManagerController(managerServiceMock, responseServiceMock);
    private final ObjectMapper mapper = new ObjectMapper();
    private final long testManagerId = 1L;
    private final Manager testManager = new Manager(testManagerId, "Harry", "box@mail.com", "0442222222");
    private final ManagerDTO testManagerDTO = new ManagerDTO(testManager);
    private final List<ManagerDTO> testListDTO = Arrays.asList(testManagerDTO);
    private final int responseEntityFieldCount = 3;
    private final String testManagerName = testManager.getName();
    private final ResponseEntity<ResponseListDTO<ManagerDTO>> successListEntity;
    private final ResponseEntity<ResponseDTO<ManagerDTO>> successOkEntity, successCreatedEntity, validationErrorEntity, persistenceErrorEntity;
    private MockMvc mockMvc;

    public ManagerControllerMockTest() {
        ResponseListDTO<ManagerDTO> testResponseListDTO = new ResponseListDTO<>(ResponseService.SUCCESSFUL, null, testListDTO);
        successListEntity = new ResponseEntity<>(testResponseListDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(List.class), eq(HttpStatus.OK))).thenReturn(successListEntity);

        ResponseDTO<ManagerDTO> testResponseDTO = new ResponseDTO<>(ResponseService.SUCCESSFUL, null, testManagerDTO);
        successOkEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(ManagerDTO.class), eq(HttpStatus.OK))).thenReturn(successOkEntity);
        doReturn(successOkEntity).when(responseServiceMock).success(anyString(), eq(HttpStatus.OK));

        successCreatedEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.CREATED);
        when(responseServiceMock.success(anyString(), any(ManagerDTO.class), eq(HttpStatus.CREATED))).thenReturn(successCreatedEntity);

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
                .standaloneSetup(managerController)
                .build();
    }

    @Test
    void whenGetManagersShouldReturnManagersJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.response.length()").value(testListDTO.size()))
                .andExpect(jsonPath("$.response[0].id").value(testManager.getId()));
    }

    @Test
    void whenGetManagerShouldReturnManagerJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/managers/" + testManagerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testManager.getId()));
    }

    @Test
    void whenCreateAManagerShouldReturnManagerJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testManagerDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testManager.getId()));
    }

    @Test
    void whenCreateAManagerShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testManagerDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testManagerDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testManagerDTO.setName(testManagerName);

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenUpdateManagerShouldReturnManagerJsonRepresentation() throws Exception {
        //given
        when(managerServiceMock.update(testManagerDTO)).thenReturn(true);

        String testName = "Jill";
        testManagerDTO.setName(testName);

        //when
        final ResultActions result = mockMvc.perform(
                put("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testManagerDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testManagerDTO.setName(testManagerName);

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.name").value(testName));
    }

    @Test
    void whenUpdateManagerShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(managerServiceMock.update(testManagerDTO)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                put("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testManagerDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

    @Test
    void whenUpdateManagerShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testManagerDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                put("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testManagerDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testManagerDTO.setName(testManagerName);
        System.out.println(result.andReturn().getResponse().getContentAsString());

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenDeleteManagerShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        when(managerServiceMock.delete(testManagerId)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/managers/" + testManagerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenDeleteManagerShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(managerServiceMock.delete(testManagerId)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/managers/" + testManagerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }


    @Test
    void whenPatchManagerShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        String newPhone = "777-77-77";
        when(managerServiceMock.patchPhone(testManagerId, newPhone)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/managers/" + testManagerId + "/phone/" + newPhone)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenPatchManagerShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        String newPhone = "777-77-77";
        when(managerServiceMock.patchPhone(testManagerId, newPhone)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/managers/" + testManagerId + "/phone/" + newPhone)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

}