package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
class GroupServiceTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GroupService groupService;

	@Test
	void givenNewGroup_whenCreate_thenCreated() {
		Group expected = Group.builder().name("T5-03").build();

		groupService.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {
		groupService.deleteById(2);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = 2 ");
		assertEquals(0, actual);
	}

	@Test
	void givenGroupWithStudents_whenDeleteById_thenNoAction() {
		groupService.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = 1");
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenGetById_thenGroupFound() {
		Group expected = Group.builder().id(2).name("A7-98").build();
		ArrayList<Student> students = new ArrayList<>();
		expected.setStudents(students);

		Group actual = groupService.getById(2);

		assertEquals(expected, actual);
	}

	@Test
	void givenGroups_whenGetAll_thenGroupsListFound() {
		Group first = Group.builder().id(1).name("T7-09").build();
		Group second = Group.builder().id(2).name("A7-98").build();
		List<Group> expected = new ArrayList<>();
		expected.add(first);
		expected.add(second);

		List<Group> actual = groupService.getAll();

		assertEquals(expected, actual);
	}
}
