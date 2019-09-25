package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Manager;
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
public class PlainJdbcManagerDAO implements BaseDAO<Manager> {

    private static final String INSERT_SQL = "INSERT INTO managers (name, email, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE managers SET name=?,email=?,phone=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM managers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM managers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM managers WHERE id=?";

    transient private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlainJdbcManagerDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Manager create(final Manager manager) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection
                        .prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, manager.getName());
                ps.setString(2, manager.getEmail());
                ps.setString(3, manager.getPhone());
                return ps;
            }
        }, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Manager(id, manager);
    }

    @Override
    public Manager get(final long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ONE_SQL,
                    BeanPropertyRowMapper.newInstance(Manager.class), id);}
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Manager> getAll() {
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_ALL_SQL);
        return rows.stream().map(row -> {
            final Manager manager = new Manager();
            manager.setId((long) row.get("id"));
            manager.setName((String) row.get("name"));
            manager.setEmail((String) row.get("email"));
            manager.setPhone((String) row.get("phone"));
            return manager;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean update(final Manager manager) {
        final int rowCount = jdbcTemplate.update(UPDATE_SQL, new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps) throws SQLException {
                ps.setString(1, manager.getName());
                ps.setString(2, manager.getEmail());
                ps.setString(3, manager.getPhone());
                ps.setLong(4, manager.getId());

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
