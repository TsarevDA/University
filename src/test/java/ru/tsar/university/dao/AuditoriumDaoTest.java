package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tsar.university.dao.AuditoriumDaoTest.TestData.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.config.SpringTestConfig;
import ru.tsar.university.model.Auditorium;

@SpringJUnitConfig(SpringTestConfig.class)
@Sql("/auditoriumData.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@WebAppConfiguration
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
		Auditorium expected = auditorium_1;

		Auditorium actual = auditoriumDao.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenAuditorium_whenUpdate_thenUpdated() {
		Auditorium expected = auditorium_3;

		auditoriumDao.update(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "auditoriums",
				"name = 'newAuditorium' and id = 1");
		assertEquals(1, actual);
	}

	@Test
	void givenAuditoriums_whenGetAll_thenAuditoriumsListFound() {
		List<Auditorium> auditoriums = new ArrayList<>();
		auditoriums.add(auditorium_1);
		auditoriums.add(auditorium_2);
		Page<Auditorium> expected = new PageImpl<>(auditoriums, pageable, auditoriums.size());

		Page<Auditorium> actual = auditoriumDao.getAll(pageable);

		assertEquals(expected, actual);
	}

	interface TestData {
		Pageable pageable = PageRequest.of(0, 5);
		Auditorium auditorium_1 = Auditorium.builder()
				.id(1)
				.name("First")
				.capacity(100)
				.build();
		Auditorium auditorium_2 = Auditorium.builder()
				.id(2)
				.name("Second")
				.capacity(500)
				.build();
		Auditorium auditorium_3 = Auditorium.builder()
				.id(1)
				.name("newAuditorium")
				.capacity(500)
				.build();
	}
}
