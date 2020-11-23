package ru.tsar.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import ru.tsar.university.model.Auditorium;

public class AuditoriumDao {

	final private String ADD_AUDITORIUM_TIME_QUERY = "INSERT INTO auditoriums(name,capacity) VALUES(?,?)";
	final private String DELETE_AUDITORIUM_QUERY = "DELETE FROM auditoriums where id =?";
	private JdbcTemplate jdbcTemplate;

	public AuditoriumDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void addAuditorium(Auditorium auditorium) {
		jdbcTemplate.update(ADD_AUDITORIUM_TIME_QUERY, auditorium.getName(), auditorium.getCapacity());
	}

	public void deleteAuditorium(int id) {
		jdbcTemplate.update(DELETE_AUDITORIUM_QUERY, id);
	}
}
