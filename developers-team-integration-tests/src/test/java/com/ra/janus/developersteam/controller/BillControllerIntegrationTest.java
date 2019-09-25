package com.ra.janus.developersteam.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.janus.developersteam.configuration.WebConfig;
import com.ra.janus.developersteam.dto.BillDTO;
import com.ra.janus.developersteam.dto.ResponseDTO;
import com.ra.janus.developersteam.dto.ResponseListDTO;
import com.ra.janus.developersteam.entity.Bill;
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

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
class BillControllerIntegrationTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final BillDTO testBillDTO = new BillDTO(new Bill(1L, Date.valueOf("2020-11-03")));
    private MockMvc mockMvc;
    @Autowired
    private BillController billController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(billController)
                .build();
    }

    @Test
    void whenGetBillDTOsShouldReturnBillDTOs() throws Exception {
        //given
        ResponseEntity<ResponseListDTO<BillDTO>> listResponseEntity = billController.getBills();
        for (BillDTO dto : listResponseEntity.getBody().getResponse()) {
            billController.deleteBill(dto.getId());
        }

        BillDTO created = createDTO(testBillDTO);
        List<BillDTO> expected = Arrays.asList(created);

        //when
        MvcResult result = mockMvc.perform(
                get("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseListDTO<BillDTO> responseListDTO =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<ResponseListDTO<BillDTO>>() {
                        });

        List<BillDTO> actual = responseListDTO.getResponse();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void whenGetBillDTOShouldReturnBillDTO() throws Exception {
        //given
        BillDTO expected = createDTO(testBillDTO);

        //when
        MvcResult result = mockMvc.perform(
                get("/bills/{id}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();
        BillDTO actual = getDTOFromMVCResult(result);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateBillShouldReturnBillDTO() throws Exception {
        //when
        BillDTO created = createDTO(testBillDTO);

        //then
        assertTrue(created instanceof BillDTO);
    }

    @Test
    void whenUpdateBillShouldReturnConfirmation() throws Exception {
        //given
        BillDTO dto = createDTO(testBillDTO);
        dto.setDocDate(new Date(12345L));

        //when
        MvcResult result = mockMvc.perform(
                put("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<BillDTO> testResponseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, testResponseDTO.getResultCode());
    }

    @Test
    void whenDeleteBillShouldReturnConfirmation() throws Exception {
        //given
        BillDTO dto = createDTO(testBillDTO);

        //when
        MvcResult result = mockMvc.perform(
                delete("/bills/{id}", dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE)).andReturn();

        ResponseDTO<BillDTO> responseDTO =
                getResponseDTOFromMvcResult(result);

        //then
        assertEquals(ResponseService.SUCCESSFUL, responseDTO.getResultCode());
    }

    private BillDTO createDTO(BillDTO dtoToCreate) throws Exception {
        MvcResult result = mockMvc.perform(
                post("/bills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoToCreate))
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE))
                .andReturn();
        return getDTOFromMVCResult(result);
    }

    private BillDTO getDTOFromMVCResult(MvcResult result) throws Exception {
        ResponseDTO<BillDTO> responseDTO = getResponseDTOFromMvcResult(result);
        return responseDTO.getDto();
    }

    private ResponseDTO<BillDTO> getResponseDTOFromMvcResult(MvcResult result) throws java.io.IOException {
        return mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDTO<BillDTO>>() {
                });
    }
}