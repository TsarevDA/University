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
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Teacher;

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql("/teacherData.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TeacherServiceTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private TeacherService teacherService;

	@Test
	void givenTeacher_whenCreate_thenCreated() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher expected = Teacher.builder().firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").courses(new ArrayList<Course>()).build();

		teacherService.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {

		teacherService.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	void givenId_whenGetById_thenTeacherFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher expected = Teacher.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").courses(new ArrayList<Course>()).build();

		Teacher actual = teacherService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeachers_whenGetAll_thenTeachersListFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<Course> courses = new ArrayList<>();
		Teacher teacher1 = Teacher.builder().firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		teacher1.setCourses(courses);
		Teacher teacher2 = Teacher.builder().firstName("Petr").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1992-05-03", formatter)).email("mail11111@mail.ru").phone("880899908080")
				.address("Petrov street, 25-5").build();
		teacher2.setCourses(courses);
		List<Teacher> expected = new ArrayList<>();
		expected.add(teacher1);
		expected.add(teacher2);

		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}

}
