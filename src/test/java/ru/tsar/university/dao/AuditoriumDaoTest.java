package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ru.tsar.university.SpringConfig;

import ru.tsar.university.model.Auditorium;

@SpringJUnitConfig(classes = SpringConfig.class)
@Sql("/schema.sql")
class AuditoriumDaoTest {

	final static private String GET_AUDITORIUMS_QUERY = "SELECT * FROM auditoriums";
	final static private String GET_BY_ID_QUERY = "SELECT * FROM auditoriums WHERE id=?";
	final static private String GET_COUNT_BY_ID_QUERY = "select count(*) FROM auditoriums WHERE id=?";
	final static private String CREATE_AUDITORIUM_QUERY = "INSERT INTO auditoriums(name,capacity) VALUES(?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private AuditoriumDao auditoriumDao;

	@Test
	void setAuditorium_whenCreate_thenCreateAuditorium() {
		Auditorium expected = new Auditorium("Name", 100);
		auditoriumDao.create(expected);

		List<Auditorium> auditoriums = jdbcTemplate.query(GET_AUDITORIUMS_QUERY, (resultSet, rowNum) -> {
			Auditorium newAuditorium = new Auditorium(resultSet.getInt("id"), resultSet.getString("name"),
					resultSet.getInt("capacity"));
			return newAuditorium;
		});

		Auditorium actual = auditoriums.get(auditoriums.size() - 1);
		assertEquals(expected, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteAuditorium() {
		Auditorium auditorium = new Auditorium("AName", 1000);
		auditoriumDao.create(auditorium);
		auditoriumDao.deleteById(auditorium.getId());
		int actual = jdbcTemplate.queryForObject(GET_COUNT_BY_ID_QUERY, Integer.class, auditorium.getId());
		assertEquals(0, actual);
	}

	@Test
	void setId_whenGetById_thenGetAuditorium() {
		Auditorium expected = new Auditorium("Name", 100);
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_AUDITORIUM_QUERY,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, expected.getName());
			ps.setInt(2, expected.getCapacity());
			return ps;
		}, holder);
		expected.setId((int) holder.getKeys().get("id"));
		Auditorium actual = auditoriumDao.getById(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	void setAuditorium_whenUpdate_thenUpdatedAuditorium() {
		Auditorium old = new Auditorium("Name", 100);
		auditoriumDao.create(old);

		Auditorium expected = new Auditorium(old.getId(), "newAuditorium", 1000);
		auditoriumDao.update(expected);

		Auditorium actual = jdbcTemplate.queryForObject(GET_BY_ID_QUERY, (resultSet, rowNum) -> {
			Auditorium newAuditorium = new Auditorium(resultSet.getInt("id"), resultSet.getString("name"),
					resultSet.getInt("capacity"));
			return newAuditorium;
		}, old.getId());

		assertEquals(expected, actual);
	}

	@Test
	void setAuditoriums_whengetAuditoriumsList_thenAuditoriumsList() {
		Auditorium first = new Auditorium("First", 100);
		auditoriumDao.create(first);

		Auditorium second = new Auditorium("Second", 500);
		auditoriumDao.create(second);

		List<Auditorium> expected = new ArrayList<>();
		expected.add(first);
		expected.add(second);

		List<Auditorium> actual = jdbcTemplate.query(GET_AUDITORIUMS_QUERY, (resultSet, rowNum) -> {
			Auditorium newAuditorium = new Auditorium(resultSet.getInt("id"), resultSet.getString("name"),
					resultSet.getInt("capacity"));
			return newAuditorium;
		});

		assertEquals(expected, actual);
	}

}
