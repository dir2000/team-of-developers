package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PlainJdbcWorkDAO implements BaseDAO<Work> {
    private static final String INSERT_SQL = "INSERT INTO works (name, price) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE works SET name=?,price=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM works";
    private static final String SELECT_ONE_SQL = "SELECT * FROM works WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM works WHERE id=?";

    transient private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlainJdbcWorkDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Work create(final Work work) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection
                        .prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, work.getName());
                ps.setBigDecimal(2, work.getPrice());
                return ps;
            }
        }, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Work(id, work);
    }

    @Override
    public Work get(final long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ONE_SQL,
                    BeanPropertyRowMapper.newInstance(Work.class), id);}
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Work> getAll() {
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_ALL_SQL);
        return rows.stream().map(row -> {
            final Work work = new Work();
            work.setId((long) row.get("id"));
            work.setName((String) row.get("name"));
            work.setPrice((BigDecimal) row.get("price"));
            return work;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean update(final Work work) {
        final int rowCount = jdbcTemplate.update(UPDATE_SQL, new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps) throws SQLException {
                ps.setString(1, work.getName());
                ps.setBigDecimal(2, work.getPrice());
                ps.setLong(3, work.getId());

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
