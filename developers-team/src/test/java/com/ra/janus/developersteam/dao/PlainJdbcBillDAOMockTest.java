package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Bill;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlainJdbcBillDAOMockTest {

    private static final String INSERT_SQL = "INSERT INTO bills (docdate) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE bills SET docdate=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM bills";
    private static final String SELECT_ONE_SQL = "SELECT * FROM bills WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM bills WHERE id=?";
    private static final long TEST_ID = 1L;
    private static final Bill TEST_BILL = new Bill(TEST_ID, new Date(System.currentTimeMillis()));

    private JdbcTemplate mockTemplate = mock(JdbcTemplate.class);
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

    private BaseDAO<Bill> billDAO = new PlainJdbcBillDAO(mockTemplate);

    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        testMap.put("id", TEST_BILL.getId());
        testMap.put("docdate", TEST_BILL.getDocDate());
        return testMap;
    }

    @Test
    void whenCreateBillShouldReturnBill() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(mockPreparedStatement);
        when(mockTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenAnswer(
                new Answer() {
                    public Object answer(InvocationOnMock invocation) throws SQLException {
                        Object[] args = invocation.getArguments();
                        PreparedStatementCreator creator = (PreparedStatementCreator) args[0];
                        creator.createPreparedStatement(mockConnection);

                        KeyHolder holder = (KeyHolder) args[1];
                        Map<String, Object> map = new HashMap<>(1);
                        map.put("Something like a generated key", Long.valueOf(1L));
                        holder.getKeyList().add(map);
                        return 1;
                    }
                });

        //when
        Bill bill = billDAO.create(TEST_BILL);

        //then
        assertEquals(TEST_BILL, bill);
    }

    //==============================

    @Test
    void whenReadBillFromDbByIdThenReturnIt() throws Exception {
        //given
        when(mockTemplate.queryForObject(eq(SELECT_ONE_SQL), any(BeanPropertyRowMapper.class), eq(TEST_ID)))
                .thenReturn(TEST_BILL);

        //when
        Bill bill = billDAO.get(TEST_ID);

        //then
        assertEquals(TEST_ID, bill.getId());
    }

    @Test
    void whenReadAbsentBillFromDbByIdThenReturnNull() throws Exception {
        //given
        when(mockTemplate.queryForObject(eq(SELECT_ONE_SQL), any(BeanPropertyRowMapper.class), eq(TEST_ID)))
                .thenThrow(new EmptyResultDataAccessException(1));

        //when
        Bill bill = billDAO.get(TEST_ID);

        //then
        assertEquals(null, bill);
    }

    @Test
    void whenReadAllBillsFromDbThenReturnNonEmptyList() throws Exception {
        //given
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(getTestEntityMap());
        when(mockTemplate.queryForList(SELECT_ALL_SQL)).thenReturn(rows);

        //when
        List<Bill> list = billDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenUpdateBillInDbThenReturnTrue() throws Exception {
        //given
        when(mockTemplate.update(eq(UPDATE_SQL), any(PreparedStatementSetter.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                PreparedStatementSetter setter = (PreparedStatementSetter) args[1];
                setter.setValues(mockPreparedStatement);
                return 1;
            }
        });

        //when
        boolean updated = billDAO.update(TEST_BILL);

        //then
        assertTrue(updated);
    }

    @Test
    void whenUpdateBillInDbThenReturnFalse() throws Exception {
        //given
        when(mockTemplate.update(eq(UPDATE_SQL), any(PreparedStatementSetter.class))).thenReturn(0);

        //when
        boolean updated = billDAO.update(TEST_BILL);

        //then
        assertFalse(updated);
    }

    @Test
    void whenDeleteBillFromDbThenReturnTrue() throws Exception {
        //given
        when(mockTemplate.update(DELETE_SQL, TEST_ID)).thenReturn(1);

        //when
        boolean deleted = billDAO.delete(TEST_ID);

        //then
        assertTrue(deleted);
    }

    @Test
    void whenDeleteBillFromDbThenReturnFalse() throws Exception {
        //given
        when(mockTemplate.update(DELETE_SQL, TEST_ID)).thenReturn(0);

        //when
        boolean deleted = billDAO.delete(TEST_ID);

        //then
        assertFalse(deleted);
    }
}