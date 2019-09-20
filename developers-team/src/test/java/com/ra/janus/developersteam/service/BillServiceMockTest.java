package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.PlainJdbcBillDAO;
import com.ra.janus.developersteam.dto.BillDTO;
import com.ra.janus.developersteam.entity.Bill;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class BillServiceMockTest {
    private static final PlainJdbcBillDAO MOCK_DAO = mock(PlainJdbcBillDAO.class);
    private static final BillService SERVICE = new BillService(MOCK_DAO);
    private static final long TEST_ID = 1L;
    private static final Bill TEST_BILL = new Bill(TEST_ID, new Date(System.currentTimeMillis()));
    private static final BillDTO TEST_BILL_DTO = new BillDTO(TEST_BILL);


    @Test
    void whenCreateShouldCreateIt() throws Exception {
        //given
        when(MOCK_DAO.create(TEST_BILL)).thenReturn(TEST_BILL);

        //when
        BillDTO actual = SERVICE.create(TEST_BILL_DTO);

        //then
        assertEquals(TEST_BILL_DTO, actual);
    }

    @Test
    void whenGetAllShouldGetAll() {
        //given
        List<Bill> testList = Arrays.asList(TEST_BILL);
        List<BillDTO> testListDtO = Arrays.asList(TEST_BILL_DTO);
        when(MOCK_DAO.getAll()).thenReturn(testList);

        //wnen
        List<BillDTO> actual = SERVICE.getAll();

        //then
        assertEquals(testListDtO, actual);
    }

    @Test
    void whenGetShouldGetIt() {
        //given
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_BILL);

        //wnen
        BillDTO actual = SERVICE.get(TEST_ID);

        //then
        assertEquals(TEST_BILL_DTO, actual);
    }

    @Test
    void whenUpdateShouldUpdateIt() {
        //given
        when(MOCK_DAO.update(any(Bill.class))).thenReturn(true);

        //wnen
        boolean isUpdated = SERVICE.update(TEST_BILL_DTO);

        //then
        assertEquals(true, isUpdated);
    }

    @Test
    void whenDeleteShouldDeleteIt() {
        //given
        when(MOCK_DAO.delete(TEST_ID)).thenReturn(true);

        //wnen
        boolean isDeleted = SERVICE.delete(TEST_ID);

        //then
        assertEquals(true, isDeleted);
    }
}