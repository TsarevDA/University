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
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;

@SpringJUnitConfig(SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/groupData.sql")
@WebAppConfiguration
class GroupDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GroupDao groupDao;

	@Test
	void givenNewGroup_whenCreate_thenCreated() {
		Group expected = Group.builder().name("T7-09").build();

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

		List<Group> groups = new ArrayList<>();
		groups.add(group_1);
		groups.add(group_2);
		Page<Group> expected = new PageImpl<>(groups, pageable, groups.size());
		Page<Group> actual = groupDao.getAll(pageable);

		assertEquals(expected, actual);
	}
	
	interface TestData {
		Pageable pageable = PageRequest.of(0, 5);
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
