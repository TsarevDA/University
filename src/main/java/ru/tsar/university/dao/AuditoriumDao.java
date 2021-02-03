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
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;

@Component
public class AuditoriumDao {

	final static private String CREATE_AUDITORIUM_QUERY = "INSERT INTO auditoriums(name,capacity) VALUES(?,?)";
	final static private String DELETE_AUDITORIUM_QUERY = "DELETE FROM auditoriums WHERE id =?";
	final static private String GET_BY_ID_QUERY = "SELECT * FROM auditoriums WHERE id=?";
	final static private String GET_AUDITORIUMS_QUERY = "SELECT * FROM auditoriums ";
	final static private String UPDATE_AUDITORIUMS_QUERY = "UPDATE auditoriums SET name=?,capacity=? WHERE id=?";

	private JdbcTemplate jdbcTemplate;
	
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
		AuditoriumRowMapper rowMapper = new AuditoriumRowMapper();
		return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
	}

	public List<Auditorium> getAuditoriumsList() {
		AuditoriumRowMapper rowMapper = new AuditoriumRowMapper();
		List<Auditorium> auditoriums = jdbcTemplate.query(GET_AUDITORIUMS_QUERY, rowMapper);
		return auditoriums;
	}
	
	public void update(Auditorium auditorium) {
		
			jdbcTemplate.update(UPDATE_AUDITORIUMS_QUERY,
				auditorium.getName(),
				auditorium.getCapacity(),
				auditorium.getId()
				);
	}
}
