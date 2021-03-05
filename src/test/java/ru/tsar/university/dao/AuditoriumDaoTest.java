package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.config.SpringTestConfig;
import ru.tsar.university.model.Auditorium;

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql("/auditoriumData.sql")
class AuditoriumDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private AuditoriumDao auditoriumDao;


	@Test
	void givenNewAuditorium_whenCreate_thenCreated() {
		Auditorium expected = new Auditorium.AuditoriumBuilder().setName("Name").setCapacity(100).build();
		
		auditoriumDao.create(expected);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "auditoriums", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	@DirtiesContext
	void givenId_whenDeleteById_thenDeleted() {
		
		auditoriumDao.deleteById(1);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "auditoriums", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	@DirtiesContext
	void givenId_whenGetById_thenAuditoriumFound() {
		Auditorium expected = new Auditorium.AuditoriumBuilder().setId(1).setName("First").setCapacity(100).build();
		
		Auditorium actual = auditoriumDao.getById(1);
		
		assertEquals(expected, actual);
	}

	@Test
	@DirtiesContext
	void givenAuditorium_whenUpdate_thenUpdated() {
		Auditorium expected = new Auditorium.AuditoriumBuilder().setId(1).setName("newAuditorium").setCapacity(1000).build();
		
		auditoriumDao.update(expected);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "auditoriums",
				"name = 'newAuditorium' and id = 1");
		assertEquals(1, actual);
	}

	@Test
	@DirtiesContext
	void givenAuditoriums_whenGetAll_thenAuditoriumsListFound() {
		Auditorium auditorium1 = new Auditorium.AuditoriumBuilder().setId(1).setName("First").setCapacity(100).build();
		Auditorium auditorium2 = new Auditorium.AuditoriumBuilder().setId(2).setName("Second").setCapacity(500).build();
		List<Auditorium> expected = new ArrayList<>();
		expected.add(auditorium1);
		expected.add(auditorium2);

		List<Auditorium> actual = auditoriumDao.getAll();
		
		assertEquals(expected, actual);
	}

}
