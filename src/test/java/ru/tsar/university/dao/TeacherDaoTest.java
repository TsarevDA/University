package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
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
class TeacherDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private TeacherDao teacherDao;

	@Test
	@DirtiesContext
	void givenTeacher_whenCreate_thenCreated() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher expected = new Teacher.TeacherBuilder().setFirstName("Ivan").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1990-01-01", formatter))
				.setEmail("mail@mail.ru").setPhone("88008080").setAddress("Ivanov street, 25-5")
				.setCourses(new ArrayList<Course>()).build();
		
		teacherDao.create(expected);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	@DirtiesContext
	void givenId_whenDeleteById_thenDeleted() {
		
		teacherDao.deleteById(1);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	@DirtiesContext
	void givenId_whenGetById_thenTeacherFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher expected = new Teacher.TeacherBuilder().setId(1).setFirstName("Ivan").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1990-01-01", formatter))
				.setEmail("mail@mail.ru").setPhone("88008080").setAddress("Ivanov street, 25-5")
				.setCourses(new ArrayList<Course>()).build();
		
		Teacher actual = teacherDao.getById(1);
		
		assertEquals(expected, actual);
	}

	@Test
	@DirtiesContext
	void givenTeachers_whenGetAll_thenTeachersListFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<Course> courses = new ArrayList<>();
		Teacher teacher1 = new Teacher.TeacherBuilder().setFirstName("Ivan").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1990-01-01", formatter))
				.setEmail("mail@mail.ru").setPhone("88008080").setAddress("Ivanov street, 25-5").build();
		teacher1.setCourses(courses);
		Teacher teacher2 = new Teacher.TeacherBuilder().setFirstName("Petr").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1992-05-03", formatter))
				.setEmail("mail11111@mail.ru").setPhone("880899908080").setAddress("Petrov street, 25-5").build();
		teacher2.setCourses(courses);
		List<Teacher> expected = new ArrayList<>();
		expected.add(teacher1);
		expected.add(teacher2);

		List<Teacher> actual = teacherDao.getAll();
		
		assertEquals(expected, actual);
	}

}
