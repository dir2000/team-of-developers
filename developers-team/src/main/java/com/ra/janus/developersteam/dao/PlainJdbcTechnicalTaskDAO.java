package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PlainJdbcTechnicalTaskDAO implements BaseDAO<Task> {
    private static final String INSERT_SQL = "INSERT INTO tasks (title, description) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE tasks SET title=?,description=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM tasks";
    private static final String SELECT_ONE_SQL = "SELECT * FROM tasks WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM tasks WHERE id=?";

    transient private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlainJdbcTechnicalTaskDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Task create(final Task task) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection
                        .prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, task.getTitle());
                ps.setString(2, task.getDescription());
                return ps;
            }
        }, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Task(id, task);
    }

    @Override
    public Task get(final long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ONE_SQL,
                    BeanPropertyRowMapper.newInstance(Task.class), id);}
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Task> getAll() {
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_ALL_SQL);
        return rows.stream().map(row -> {
            final Task task = new Task();
            task.setId((long) row.get("id"));
            task.setTitle((String) row.get("title"));
            task.setDescription((String) row.get("description"));
            return task;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean update(final Task task) {
        final int rowCount = jdbcTemplate.update(UPDATE_SQL, new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps) throws SQLException {
                ps.setString(1, task.getTitle());
                ps.setString(2, task.getDescription());
                ps.setLong(3, task.getId());

            }
        });
        return rowCount != 0;
    }

    @Override
    public boolean delete(final long id) {
        final int rowCount = jdbcTemplate.update(DELETE_SQL, id);
        return rowCount != 0;
    }
}
