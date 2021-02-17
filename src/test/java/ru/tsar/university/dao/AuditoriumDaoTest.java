package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.SpringTestConfig;
import ru.tsar.university.model.Auditorium;

class AuditoriumDaoTest {

	private AnnotationConfigApplicationContext context;
	private JdbcTemplate jdbcTemplate;
	private AuditoriumDao auditoriumDao;

	@BeforeEach
	void setUp() {
		context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		context.getBean("databasePopulator", DatabasePopulator.class);
		this.jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		this.auditoriumDao = context.getBean("auditoriumDao", AuditoriumDao.class);
	}

	@AfterEach
	void setDown() {
		context.close();
	}

	@Test
	void setAuditorium_whenCreate_thenCreateAuditorium() {
		Auditorium expected = new Auditorium("Name", 100);
		auditoriumDao.create(expected);
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "auditoriums", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteAuditorium() {
		Auditorium auditorium = new Auditorium("AName", 1000);
		auditoriumDao.create(auditorium);
		auditoriumDao.deleteById(auditorium.getId());
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "auditoriums", "id = " + auditorium.getId());
		assertEquals(0, actual);
	}

	@Test
	void setId_whenGetById_thenGetAuditorium() {

		Auditorium expected = new Auditorium("Name", 100);
		auditoriumDao.create(expected);
		Auditorium actual = auditoriumDao.getById(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	void setAuditorium_whenUpdate_thenUpdatedAuditorium() {

		Auditorium old = new Auditorium("Name", 100);
		auditoriumDao.create(old);
		Auditorium expected = new Auditorium(old.getId(), "newAuditorium", 1000);
		auditoriumDao.update(expected);
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "auditoriums",
				"name = 'newAuditorium' and id = " + old.getId());

		assertEquals(1, actual);
	}

	@Test
	void setAuditoriums_whenGetAll_thenAuditoriumsList() {
		Auditorium first = new Auditorium("First", 100);
		auditoriumDao.create(first);

		Auditorium second = new Auditorium("Second", 500);
		auditoriumDao.create(second);

		List<Auditorium> expected = new ArrayList<>();
		expected.add(first);
		expected.add(second);

		List<Auditorium> actual = auditoriumDao.getAll();
		assertEquals(expected, actual);
	}

}
