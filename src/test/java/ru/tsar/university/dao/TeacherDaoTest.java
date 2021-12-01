package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tsar.university.dao.TeacherDaoTest.TestData.*;

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
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Teacher;

@SpringJUnitConfig(SpringTestConfig.class)
@Sql("/teacherData.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@WebAppConfiguration
class TeacherDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private TeacherDao teacherDao;

	@Test
	void givenTeacher_whenCreate_thenCreated() {
		Teacher expected = Teacher.builder().firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").courses(new ArrayList<Course>()).build();

		teacherDao.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {

		teacherDao.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	void givenId_whenGetById_thenTeacherFound() {
		Teacher expected = teacher_1;

		Teacher actual = teacherDao.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeachers_whenGetAll_thenTeachersListFound() {

		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher_1);
		teachers.add(teacher_2);
		Page<Teacher> expected = new PageImpl<>(teachers, pageable, teachers.size());
		Page<Teacher> actual = teacherDao.getAll(pageable);

		assertEquals(expected, actual);
	}

	interface TestData {
		Pageable pageable = PageRequest.of(0, 5);
		Course course_1 = Course.builder()
				.id(1)
				.name("Math")
				.description("Science about numbers")
				.build();
		Course course_2 = Course.builder()
				.id(2)
				.name("Astronomy")
				.description("Science about stars and deep space")
				.build();
		
		List<Course> courses_1 = Arrays.asList(course_1);
		List<Course> courses_2 = Arrays.asList(course_2);
		Teacher teacher_1 = Teacher.builder()
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.courses(courses_1)
				.build();
		
		Teacher teacher_2 = Teacher.builder()
				.firstName("Petr")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1992,Month.MAY,3))
				.email("mail11111@mail.ru")
				.phone("880899908080")
				.address("Petrov street, 25-5")
				.courses(courses_2)
				.build();
	}
}