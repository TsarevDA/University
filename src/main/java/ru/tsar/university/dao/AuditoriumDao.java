package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.dao.mapper.AuditoriumRowMapper;
import ru.tsar.university.model.Auditorium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuditoriumDao {

	private static final String CREATE_AUDITORIUM_QUERY = "INSERT INTO auditoriums(name, capacity) VALUES(?,?)";
	private static final String DELETE_AUDITORIUM_QUERY = "DELETE FROM auditoriums WHERE id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM auditoriums WHERE id=?";
	private static final String GET_ALL_QUERY = "SELECT * FROM auditoriums ";
	private static final String UPDATE_AUDITORIUMS_QUERY = "UPDATE auditoriums SET name=?, capacity=? WHERE id=?";
	private static final String GET_BY_NAME_QUERY = "SELECT * FROM auditoriums WHERE name = ?";
	private static final String GET_BY_DAY_TIME_AUDITORIUM_QUERY = "SELECT count(*) FROM lessons WHERE day=? AND lesson_time_id = ? AND auditorium_id =?";

	private JdbcTemplate jdbcTemplate;
	private AuditoriumRowMapper rowMapper;
	private final Logger log = LoggerFactory.getLogger(AuditoriumDao.class);
	
	public AuditoriumDao(JdbcTemplate jdbcTemplate, AuditoriumRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public void create(Auditorium auditorium) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_AUDITORIUM_QUERY,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, auditorium.getName());
			ps.setInt(2, auditorium.getCapacity());
			return ps;
		}, holder);
		auditorium.setId((int) holder.getKeys().get("id"));
		log.info("Call create: {}", auditorium);
	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_AUDITORIUM_QUERY, id);
		log.info("Call deleteById, id: {}", id);
	}

	public Auditorium getById(int id) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			log.warn("EmptyResultSet, getById: {}",id);
			return null;
		}
	}

	public List<Auditorium> getAll() {
		return jdbcTemplate.query(GET_ALL_QUERY, rowMapper);
	}

	public void update(Auditorium auditorium) {
		jdbcTemplate.update(UPDATE_AUDITORIUMS_QUERY, auditorium.getName(), auditorium.getCapacity(),
				auditorium.getId());
		log.info("Call update: {}", auditorium);
	}

	public Auditorium getByName(Auditorium auditorium) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_NAME_QUERY, rowMapper, auditorium.getName());
		} catch (EmptyResultDataAccessException e) {
			log.warn("EmptyResultSet, getByName: {}", auditorium.getName());
			return null;
		}
	}

}
