package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Project;
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

class PlainJdbcProjectDAOMockTest {

    private static final String INSERT_SQL = "INSERT INTO projects (name, description, status, eta) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE projects SET name=?,description=?,status=?,eta=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM projects";
    private static final String SELECT_ONE_SQL = "SELECT * FROM projects WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM projects WHERE id=?";
    private static final long TEST_ID = 1L;
    private static final Project TEST_PROJECT = new Project(TEST_ID, "Mock Tests", "Test project with h2 DB", "WIP",Date.valueOf("2019-05-30"));

    private JdbcTemplate mockTemplate = mock(JdbcTemplate.class);
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

    private BaseDAO<Project> projectDAO = new PlainJdbcProjectDAO(mockTemplate);

    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        testMap.put("id", TEST_PROJECT.getId());
        testMap.put("name", TEST_PROJECT.getName());
        testMap.put("description", TEST_PROJECT.getDescription());
        testMap.put("status", TEST_PROJECT.getStatus());
        testMap.put("eta", TEST_PROJECT.getEta());
        return testMap;
    }

    @Test
    void whenCreateProjectShouldReturnProject() throws Exception {
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
        Project project = projectDAO.create(TEST_PROJECT);

        //then
        assertEquals(TEST_PROJECT, project);
    }

    //==============================

    @Test
    void whenReadProjectFromDbByIdThenReturnIt() throws Exception {
        //given
        when(mockTemplate.queryForObject(eq(SELECT_ONE_SQL), any(BeanPropertyRowMapper.class), eq(TEST_ID)))
                .thenReturn(TEST_PROJECT);

        //when
        Project project = projectDAO.get(TEST_ID);

        //then
        assertEquals(TEST_ID, project.getId());
    }

    @Test
    void whenReadAbsentProjectFromDbByIdThenReturnNull() throws Exception {
        //given
        when(mockTemplate.queryForObject(eq(SELECT_ONE_SQL), any(BeanPropertyRowMapper.class), eq(TEST_ID)))
                .thenThrow(new EmptyResultDataAccessException(1));

        //when
        Project project = projectDAO.get(TEST_ID);

        //then
        assertEquals(null, project);
    }

    @Test
    void whenReadAllProjectsFromDbThenReturnNonEmptyList() throws Exception {
        //given
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(getTestEntityMap());
        when(mockTemplate.queryForList(SELECT_ALL_SQL)).thenReturn(rows);

        //when
        List<Project> list = projectDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenUpdateProjectInDbThenReturnTrue() throws Exception {
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
        boolean updated = projectDAO.update(TEST_PROJECT);

        //then
        assertTrue(updated);
    }

    @Test
    void whenUpdateProjectInDbThenReturnFalse() throws Exception {
        //given
        when(mockTemplate.update(eq(UPDATE_SQL), any(PreparedStatementSetter.class))).thenReturn(0);

        //when
        boolean updated = projectDAO.update(TEST_PROJECT);

        //then
        assertFalse(updated);
    }

    @Test
    void whenDeleteProjectFromDbThenReturnTrue() throws Exception {
        //given
        when(mockTemplate.update(DELETE_SQL, TEST_ID)).thenReturn(1);

        //when
        boolean deleted = projectDAO.delete(TEST_ID);

        //then
        assertTrue(deleted);
    }

    @Test
    void whenDeleteProjectFromDbThenReturnFalse() throws Exception {
        //given
        when(mockTemplate.update(DELETE_SQL, TEST_ID)).thenReturn(0);

        //when
        boolean deleted = projectDAO.delete(TEST_ID);

        //then
        assertFalse(deleted);
    }
}