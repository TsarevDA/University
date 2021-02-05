package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.mapper.AuditoriumRowMapper;
import ru.tsar.university.model.Auditorium;

@Component
public class AuditoriumDao {

	private static final String CREATE_AUDITORIUM_QUERY = "INSERT INTO auditoriums(name,capacity) VALUES(?,?)";
	private static final String DELETE_AUDITORIUM_QUERY = "DELETE FROM auditoriums WHERE id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM auditoriums WHERE id=?";
	private static final String GET_AUDITORIUMS_QUERY = "SELECT * FROM auditoriums ";
	private static final String UPDATE_AUDITORIUMS_QUERY = "UPDATE auditoriums SET name=?,capacity=? WHERE id=?";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private AuditoriumRowMapper rowMapper;

	@Autowired
	public AuditoriumDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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
	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_AUDITORIUM_QUERY, id);
	}

	public Auditorium getById(int id) {
		return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
	}

	public List<Auditorium> getAll() {
		return jdbcTemplate.query(GET_AUDITORIUMS_QUERY, rowMapper);
	}

	public void update(Auditorium auditorium) {
		jdbcTemplate.update(UPDATE_AUDITORIUMS_QUERY, auditorium.getName(), auditorium.getCapacity(),
				auditorium.getId());
	}
}
