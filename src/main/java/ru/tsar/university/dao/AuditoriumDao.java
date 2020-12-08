package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Auditorium;

@Component
public class AuditoriumDao {

	final static private String CREATE_AUDITORIUM_QUERY = "INSERT INTO auditoriums(auditorium_name,capacity) VALUES(?,?)";
	final static private String DELETE_AUDITORIUM_QUERY = "DELETE FROM auditoriums WHERE id =?";
	final static private String GET_BY_ID_REQUEST = "SELECT a.* FROM auditoriums a WHERE id=?";

	private JdbcTemplate jdbcTemplate;

	public AuditoriumDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void create(Auditorium auditorium) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		// jdbcTemplate.update(CREATE_AUDITORIUM_QUERY, auditorium.getName(),
		// auditorium.getCapacity());
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
		Auditorium auditorium = jdbcTemplate.queryForObject(GET_BY_ID_REQUEST, (resultSet, rowNum) -> {
			Auditorium newAuditorium = new Auditorium(id, resultSet.getString("auditorium_name"),
					resultSet.getInt("capacity"));
			return newAuditorium;
		}, id);
		return auditorium;
	}
}
