package com.ra.janus.developersteam.service;

import com.ra.janus.developersteam.dao.PlainJdbcCustomerDAO;
import com.ra.janus.developersteam.dto.CustomerDTO;
import com.ra.janus.developersteam.entity.Customer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CustomerServiceMockTest {
    private static final PlainJdbcCustomerDAO MOCK_DAO = mock(PlainJdbcCustomerDAO.class);
    private static final CustomerService SERVICE = new CustomerService(MOCK_DAO);
    private static final long TEST_ID = 1L;
    private static final Customer TEST_CUSTOMER = new Customer(TEST_ID, "John", "Home", "911");
    private static final CustomerDTO TEST_CUSTOMER_DTO = new CustomerDTO(TEST_CUSTOMER);


    @Test
    void whenCreateShouldCreateIt() throws Exception {
        //given
        when(MOCK_DAO.create(TEST_CUSTOMER)).thenReturn(TEST_CUSTOMER);

        //when
        CustomerDTO actual = SERVICE.create(TEST_CUSTOMER_DTO);

        //then
        assertEquals(TEST_CUSTOMER_DTO, actual);
    }

    @Test
    void whenGetAllShouldGetAll() {
        //given
        List<Customer> testList = Arrays.asList(TEST_CUSTOMER);
        List<CustomerDTO> testListDtO = Arrays.asList(TEST_CUSTOMER_DTO);
        when(MOCK_DAO.getAll()).thenReturn(testList);

        //wnen
        List<CustomerDTO> actual = SERVICE.getAll();

        //then
        assertEquals(testListDtO, actual);
    }

    @Test
    void whenGetShouldGetIt() {
        //given
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_CUSTOMER);

        //wnen
        CustomerDTO actual = SERVICE.get(TEST_ID);

        //then
        assertEquals(TEST_CUSTOMER_DTO, actual);
    }

    @Test
    void whenUpdateShouldUpdateIt() {
        //given
        when(MOCK_DAO.update(any(Customer.class))).thenReturn(true);

        //wnen
        boolean isUpdated = SERVICE.update(TEST_CUSTOMER_DTO);

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


    @Test
    void whenPatchShouldPatchIt() {
        //given
        when(MOCK_DAO.get(TEST_ID)).thenReturn(TEST_CUSTOMER);
        when(MOCK_DAO.update(any(Customer.class))).thenReturn(true);

        //wnen
        boolean isPatched = SERVICE.patchAddress(TEST_ID, "Some new address");

        //then
        assertEquals(true, isPatched);
    }
}