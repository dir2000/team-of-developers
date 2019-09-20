package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Work;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlainJdbcWorkDAOMockTest {

    private static final String INSERT_SQL = "INSERT INTO works (name, price) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE works SET name=?,price=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM works";
    private static final String SELECT_ONE_SQL = "SELECT * FROM works WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM works WHERE id=?";
    private static final long TEST_ID = 1L;
    private static final Work TEST_WORK = new Work(TEST_ID, "name", new BigDecimal(7));

    private JdbcTemplate mockTemplate = mock(JdbcTemplate.class);
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

    private BaseDAO<Work> workDAO = new PlainJdbcWorkDAO(mockTemplate);

    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        testMap.put("id", TEST_WORK.getId());
        testMap.put("name", TEST_WORK.getName());
        testMap.put("address", TEST_WORK.getPrice());
        return testMap;
    }

    @Test
    void whenCreateWorkShouldReturnWork() throws Exception {
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
        Work work = workDAO.create(TEST_WORK);

        //then
        assertEquals(TEST_WORK, work);
    }

    //==============================

    @Test
    void whenReadWorkFromDbByIdThenReturnIt() throws Exception {
        //given
        when(mockTemplate.queryForObject(eq(SELECT_ONE_SQL), any(BeanPropertyRowMapper.class), eq(TEST_ID)))
                .thenReturn(TEST_WORK);

        //when
        Work work = workDAO.get(TEST_ID);

        //then
        assertEquals(TEST_ID, work.getId());
    }

    @Test
    void whenReadAbsentWorkFromDbByIdThenReturnNull() throws Exception {
        //given
        when(mockTemplate.queryForObject(eq(SELECT_ONE_SQL), any(BeanPropertyRowMapper.class), eq(TEST_ID)))
                .thenThrow(new EmptyResultDataAccessException(1));

        //when
        Work work = workDAO.get(TEST_ID);

        //then
        assertEquals(null, work);
    }

    @Test
    void whenReadAllWorksFromDbThenReturnNonEmptyList() throws Exception {
        //given
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(getTestEntityMap());
        when(mockTemplate.queryForList(SELECT_ALL_SQL)).thenReturn(rows);

        //when
        List<Work> list = workDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenUpdateWorkInDbThenReturnTrue() throws Exception {
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
        boolean updated = workDAO.update(TEST_WORK);

        //then
        assertTrue(updated);
    }

    @Test
    void whenUpdateWorkInDbThenReturnFalse() throws Exception {
        //given
        when(mockTemplate.update(eq(UPDATE_SQL), any(PreparedStatementSetter.class))).thenReturn(0);

        //when
        boolean updated = workDAO.update(TEST_WORK);

        //then
        assertFalse(updated);
    }

    @Test
    void whenDeleteWorkFromDbThenReturnTrue() throws Exception {
        //given
        when(mockTemplate.update(DELETE_SQL, TEST_ID)).thenReturn(1);

        //when
        boolean deleted = workDAO.delete(TEST_ID);

        //then
        assertTrue(deleted);
    }

    @Test
    void whenDeleteWorkFromDbThenReturnFalse() throws Exception {
        //given
        when(mockTemplate.update(DELETE_SQL, TEST_ID)).thenReturn(0);

        //when
        boolean deleted = workDAO.delete(TEST_ID);

        //then
        assertFalse(deleted);
    }
}