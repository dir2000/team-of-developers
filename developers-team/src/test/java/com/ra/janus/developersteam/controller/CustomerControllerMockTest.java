package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.dto.CustomerDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Customer;
import com.ra.janus.developersteam.service.CustomerService;
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

class CustomerControllerMockTest {
    private final CustomerService customerServiceMock = mock(CustomerService.class);
    private final ResponseService responseServiceMock = mock(ResponseService.class);
    private final CustomerController customerController = new CustomerController(customerServiceMock, responseServiceMock);
    private final ObjectMapper mapper = new ObjectMapper();
    private final long testCustomerId = 1L;
    private final Customer testCustomer = new Customer(testCustomerId, "Harry", "Home", "0442222222");
    private final CustomerDTO testCustomerDTO = new CustomerDTO(testCustomer);
    private final List<CustomerDTO> testListDTO = Arrays.asList(testCustomerDTO);
    private final int responseEntityFieldCount = 3;
    private final String testCustomerName = testCustomer.getName();
    private final ResponseEntity<ResponseListDTO<CustomerDTO>> successListEntity;
    private final ResponseEntity<ResponseDTO<CustomerDTO>> successOkEntity, successCreatedEntity, validationErrorEntity, persistenceErrorEntity;
    private MockMvc mockMvc;

    public CustomerControllerMockTest() {
        ResponseListDTO<CustomerDTO> testResponseListDTO = new ResponseListDTO<>(ResponseService.SUCCESSFUL, null, testListDTO);
        successListEntity = new ResponseEntity<>(testResponseListDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(List.class), eq(HttpStatus.OK))).thenReturn(successListEntity);

        ResponseDTO<CustomerDTO> testResponseDTO = new ResponseDTO<>(ResponseService.SUCCESSFUL, null, testCustomerDTO);
        successOkEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.OK);
        when(responseServiceMock.success(anyString(), any(CustomerDTO.class), eq(HttpStatus.OK))).thenReturn(successOkEntity);
        doReturn(successOkEntity).when(responseServiceMock).success(anyString(), eq(HttpStatus.OK));

        successCreatedEntity = new ResponseEntity<>(testResponseDTO, HttpStatus.CREATED);
        when(responseServiceMock.success(anyString(), any(CustomerDTO.class), eq(HttpStatus.CREATED))).thenReturn(successCreatedEntity);

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
                .standaloneSetup(customerController)
                .build();
    }

    @Test
    void whenGetCustomersShouldReturnCustomersJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.response.length()").value(testListDTO.size()))
                .andExpect(jsonPath("$.response[0].id").value(testCustomer.getId()));
    }

    @Test
    void whenGetCustomerShouldReturnCustomerJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                get("/customers/" + testCustomerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testCustomer.getId()));
    }

    @Test
    void whenCreateACustomerShouldReturnCustomerJsonRepresentation() throws Exception {
        //when
        final ResultActions result = mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testCustomerDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.id").value(testCustomer.getId()));
    }

    @Test
    void whenCreateACustomerShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testCustomerDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testCustomerDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testCustomerDTO.setName(testCustomerName);

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(responseEntityFieldCount))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenUpdateCustomerShouldReturnCustomerJsonRepresentation() throws Exception {
        //given
        when(customerServiceMock.update(testCustomerDTO)).thenReturn(true);

        String testName = "Jill";
        testCustomerDTO.setName(testName);

        //when
        final ResultActions result = mockMvc.perform(
                put("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testCustomerDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testCustomerDTO.setName(testCustomerName);

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL))
                .andExpect(jsonPath("$.dto.name").value(testName));
    }

    @Test
    void whenUpdateCustomerShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(customerServiceMock.update(testCustomerDTO)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                put("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testCustomerDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

    @Test
    void whenUpdateCustomerShouldReturnValidationErrorJsonRepresentation() throws Exception {
        //given
        testCustomerDTO.setName(null);

        //when
        final ResultActions result = mockMvc.perform(
                put("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testCustomerDTO))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
        testCustomerDTO.setName(testCustomerName);
        System.out.println(result.andReturn().getResponse().getContentAsString());

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.VALIDATION_ERROR));
    }

    @Test
    void whenDeleteCustomerShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        when(customerServiceMock.delete(testCustomerId)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/customers/" + testCustomerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenDeleteCustomerShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        when(customerServiceMock.delete(testCustomerId)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                delete("/customers/" + testCustomerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }


    @Test
    void whenPatchCustomerShouldReturnConfirmationJsonRepresentation() throws Exception {
        //given
        String newAddress = "Honolulu";
        when(customerServiceMock.patchAddress(testCustomerId, newAddress)).thenReturn(true);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/customers/" + testCustomerId + "/address/" + newAddress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.SUCCESSFUL));

    }


    @Test
    void whenPatchCustomerShouldReturnPersistenceErrorJsonRepresentation() throws Exception {
        //given
        String newAddress = "Honolulu";
        when(customerServiceMock.patchAddress(testCustomerId, newAddress)).thenReturn(false);

        //when
        final ResultActions result = mockMvc.perform(
                patch("/customers/" + testCustomerId + "/address/" + newAddress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        //then
        result.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resultCode").value(ResponseService.PERSISTENCE_ERROR));
    }

}