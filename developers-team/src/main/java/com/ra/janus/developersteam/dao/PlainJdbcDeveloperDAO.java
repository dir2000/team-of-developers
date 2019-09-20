package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Developer;
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
public class PlainJdbcDeveloperDAO implements BaseDAO<Developer> {
    private static final String INSERT_SQL = "INSERT INTO developers (name) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE developers SET name=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM developers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM developers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM developers WHERE id=?";

    transient private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlainJdbcDeveloperDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Developer create(final Developer developer) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection
                        .prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, developer.getName());
                return ps;
            }
        }, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Developer(id, developer);
    }

    @Override
    public Developer get(final long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ONE_SQL,
                    BeanPropertyRowMapper.newInstance(Developer.class), id);}
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Developer> getAll() {
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_ALL_SQL);
        return rows.stream().map(row -> {
            final Developer developer = new Developer();
            developer.setId((long) row.get("id"));
            developer.setName((String) row.get("name"));
            return developer;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean update(final Developer developer) {
        final int rowCount = jdbcTemplate.update(UPDATE_SQL, new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps) throws SQLException {
                ps.setString(1, developer.getName());
                ps.setLong(2, developer.getId());

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
