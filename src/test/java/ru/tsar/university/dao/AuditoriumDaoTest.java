package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.config.SpringTestConfig;
import ru.tsar.university.model.Auditorium;

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql("/auditoriumData.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AuditoriumDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private AuditoriumDao auditoriumDao;

	@Test
	void givenNewAuditorium_whenCreate_thenCreated() {
		Auditorium expected = Auditorium.builder().name("Name").capacity(100).build();
		
		auditoriumDao.create(expected);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "auditoriums", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {	
		
		auditoriumDao.deleteById(1);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "auditoriums", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	void givenId_whenGetById_thenAuditoriumFound() {
		Auditorium expected = new Auditorium.AuditoriumBuilder().id(1).name("First").capacity(100).build();
		
		Auditorium actual = auditoriumDao.getById(1);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenAuditorium_whenUpdate_thenUpdated() {
		Auditorium expected = Auditorium.builder().id(1).name("newAuditorium").capacity(1000).build();
				
		auditoriumDao.update(expected);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "auditoriums",
				"name = 'newAuditorium' and id = 1");
		assertEquals(1, actual);
	}

	@Test	
	void givenAuditoriums_whenGetAll_thenAuditoriumsListFound() {
		Auditorium auditorium1 = Auditorium.builder().id(1).name("First").capacity(100).build();
		Auditorium auditorium2 = Auditorium.builder().id(2).name("Second").capacity(500).build();
		List<Auditorium> expected = new ArrayList<>();
		expected.add(auditorium1);
		expected.add(auditorium2);

		List<Auditorium> actual = auditoriumDao.getAll();
		
		assertEquals(expected, actual);
	}

}
