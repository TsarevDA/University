package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.TablesCreation;
import ru.tsar.university.model.Auditorium;


class AuditoriumDaoTest {

	final static private String GET_AUDITORIUMS_REQUEST = "SELECT a.* FROM auditoriums a";
	final static private String GET_COUNT_BY_ID_REQUEST = "select count(*) FROM auditoriums WHERE id=?";
	final static private String CREATE_AUDITORIUM_QUERY = "INSERT INTO auditoriums(auditorium_name,capacity) VALUES(?,?)";

	private JdbcTemplate jdbcTemplate;
	private AuditoriumDao auditoriumDao;
	
	@BeforeEach
	void setUp() throws IOException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		auditoriumDao = context.getBean("auditoriumDao", AuditoriumDao.class);
		jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		TablesCreation tablesCreation = context.getBean("tablesCreation", TablesCreation.class);
		tablesCreation.createTables();
	}


	@Test
	void setAuditorium_whenCreate_thenCreateAuditorium() {
		Auditorium expected = new Auditorium("Name", 100);
		auditoriumDao.create(expected);

		List<Auditorium> auditoriums = jdbcTemplate.query(GET_AUDITORIUMS_REQUEST, (resultSet, rowNum) -> {
			Auditorium newAuditorium = new Auditorium(resultSet.getInt("id"), resultSet.getString("auditorium_name"),
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
		int actual = jdbcTemplate.queryForObject(GET_COUNT_BY_ID_REQUEST, Integer.class, auditorium.getId());
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

}
