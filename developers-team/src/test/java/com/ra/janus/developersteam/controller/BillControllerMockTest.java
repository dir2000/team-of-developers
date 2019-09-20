package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.dto.BillDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Bill;
import com.ra.janus.developersteam.service.BillService;
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

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BillControllerMockTest {
    private final BillService billServiceMock = mock(BillService.class);
    private final ResponseService responseServiceMock = mock(ResponseService.class);
    private final BillController billController = new BillController(billServiceMock, responseServiceMock);
    private final ObjectMapper mapper = new ObjectMapper();
    private final long testBillId = 1L;
    private final Bill testBill = new Bill(testBillId, Date.valueOf("2020-11-03"));
    private final BillDTO testBillDTO = new BillDTO(testBill);
    private final List<BillDTO> testListDTO = Arrays.asList(testBillDTO);
    private final int responseEntityFieldCount = 3;
    private final Date testBillDate = testBill.getDocDate();
    private final ResponseEntity<ResponseListDTO<BillDTO>> successListEntity;
    private final ResponseEntity<ResponseDTO<BillDTO>> successOkEntity, successCreatedEntity, validationErrorEntity, persistenceErrorEntity;
    private MockMvc mockMvc;

    public BillControllerMockTest() {
        ResponseListDTO<BillDTO> testResponseListDTO = new ResponseListDTO<>(ResponseService.SUCCESSFUL, null, testListDTO);
        successListEntity = new ResponseEntity<>(testResponseListDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(List.class), eq(HttpStatus.OK))).thenReturn(successListEntity);

        ResponseDTO<BillDTO> testResponseDTO = new ResponseDTO<>(ResponseService.SUCCESSFUL, null, testBillDTO);
        successOkEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(BillDTO.class), eq(HttpStatus.OK))).thenReturn(successOkEntity);
        doReturn(successOkEntity).when(responseServiceMock).success(anyString(), eq(HttpStatus.OK));

        successCreatedEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.CREATED);
        when(responseServiceMock.success(anyString(), any(BillDTO.class), eq(HttpStatus.CREATED))).thenReturn(successCreatedEntity);

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
                .standaloneSetup(billController)
                .build();
    }

    @Test
    void whenGetBillsShouldReturnBillsJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.response.length()").value(testListDTO.size()))
                .andExpect(jsonPath("$.response[0].id").value(testBill.getId()));
    }

    @Test
    void whenGetBillShouldReturnBillJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/bills/" + testBillId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testBill.getId()));
    }

    @Test
    void whenCreateABillShouldReturnBillJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                post("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testBillDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testBill.getId()));
    }

    @Test
    void whenCreateABillShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testBillDTO.setDocDate(null);

        //when
        final ResultActions result = mockMvc.perform(
                post("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testBillDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testBillDTO.setDocDate(testBillDate);

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenUpdateBillShouldReturnBillJsonRepresentation() throws Exception {
        //given
        when(billServiceMock.update(testBillDTO)).thenReturn(true);

        long testDateValue = 12345L;
        testBillDTO.setDocDate(new Date(testDateValue));

        //when
        final ResultActions result = mockMvc.perform(
                put("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testBillDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testBillDTO.setDocDate(testBillDate);

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.docDate").value(testDateValue));
    }

    @Test
    void whenUpdateBillShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(billServiceMock.update(testBillDTO)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                put("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testBillDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

    @Test
    void whenUpdateBillShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testBillDTO.setDocDate(null);

        //when
        final ResultActions result = mockMvc.perform(
                put("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testBillDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testBillDTO.setDocDate(testBillDate);
        System.out.println(result.andReturn().getResponse().getContentAsString());

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenDeleteBillShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        when(billServiceMock.delete(testBillId)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/bills/" + testBillId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenDeleteBillShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(billServiceMock.delete(testBillId)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/bills/" + testBillId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }
}