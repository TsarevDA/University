package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tsar.university.dao.GroupDaoTest.TestData.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.config.SpringTestConfig;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/groupData.sql")
class GroupDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GroupDao groupDao;

	@Test
	void givenNewGroup_whenCreate_thenCreated() {
		Group expected = Group.builder()
				.name("T7-09")
				.build();

		groupDao.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {
		groupDao.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = 1 ");
		assertEquals(0, actual);
	}

	@Test
	void givenId_whenGetById_thenGroupFound() {
		Group expected = group_2;

		Group actual = groupDao.getById(2);

		assertEquals(expected, actual);
	}

	@Test
	void givenGroups_whenGetAll_thenGroupsListFound() {

		List<Group> expected = new ArrayList<>();
		expected.add(group_1);
		expected.add(group_2);

		List<Group> actual = groupDao.getAll();

		assertEquals(expected, actual);
	}
	
	interface TestData {
		Student student = Student.builder()
				.id(1)
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.build();
		List<Student> students_1 = new ArrayList<>();
		List<Student> students_2 = Arrays.asList(student);
		Group group_1 = Group.builder()
				.id(1)
				.name("T7-09")
				.students(students_1)
				.build();
		Group group_2 = Group.builder()
				.id(2)
				.name("A7-98")
				.students(students_2)
				.build();
	}
}
