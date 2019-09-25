package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.dto.CustomerDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Customer;
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
class CustomerControllerIntegrationTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final CustomerDTO testCustomerDTO = new CustomerDTO(new Customer(1L, "John", "Home", "12345"));
    private MockMvc mockMvc;
    @Autowired
    private CustomerController customerController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .build();
    }

    @Test
    void whenGetCustomerDTOsShouldReturnCustomerDTOs() throws Exception {
        //given
        ResponseEntity<ResponseListDTO<CustomerDTO>> listResponseEntity = customerController.getCustomers();
        for (CustomerDTO dto : listResponseEntity.getBody().getResponse()) {
            customerController.deleteCustomer(dto.getId());
        }

        CustomerDTO created = createDTO(testCustomerDTO);
        List<CustomerDTO> expected = Arrays.asList(created);

        //when
        MvcResult result = mockMvc.perform(
                get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseListDTO<CustomerDTO> responseListDTO =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<ResponseListDTO<CustomerDTO>>() {
                        });

        List<CustomerDTO> actual = responseListDTO.getResponse();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void whenGetCustomerDTOShouldReturnCustomerDTO() throws Exception {
        //given
        CustomerDTO expected = createDTO(testCustomerDTO);

        //when
        MvcResult result = mockMvc.perform(
                get("/customers/{id}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();
        CustomerDTO actual = getDTOFromMVCResult(result);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateCustomerShouldReturnCustomerDTO() throws Exception {
        //when
        CustomerDTO created = createDTO(testCustomerDTO);

        //then
        assertTrue(created instanceof CustomerDTO);
    }

    @Test
    void whenUpdateCustomerShouldReturnConfirmation() throws Exception {
        //given
        CustomerDTO dto = createDTO(testCustomerDTO);
        dto.setName("Dyatel");

        //when
        MvcResult result = mockMvc.perform(
                put("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<CustomerDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    @Test
    void whenDeleteCustomerShouldReturnConfirmation() throws Exception {
        //given
        CustomerDTO dto = createDTO(testCustomerDTO);

        //when
        MvcResult result = mockMvc.perform(
                delete("/customers/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<CustomerDTO> responseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, responseDTO.getResultCode());
    }

    @Test
    void whenPatchCustomerShouldReturnConfirmation() throws Exception {
        //given
        CustomerDTO dto = createDTO(testCustomerDTO);

        //when
        MvcResult result = mockMvc.perform(
                patch("/customers/{id}/address/{newAddress}", dto.getId(), "Somali")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<CustomerDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    private CustomerDTO createDTO(CustomerDTO dtoToCreate) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoToCreate))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE))
                .andReturn();
        return getDTOFromMVCResult(result);
    }

    private CustomerDTO getDTOFromMVCResult(MvcResult result) throws Exception {
        ResponseDTO<CustomerDTO> responseDTO = getResponseDTOFromMvcResult(result);
        return responseDTO.getDto();
    }

    private ResponseDTO<CustomerDTO> getResponseDTOFromMvcResult(MvcResult result) throws java.io.IOException {
        return mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDTO<CustomerDTO>>() {
                });
    }
}