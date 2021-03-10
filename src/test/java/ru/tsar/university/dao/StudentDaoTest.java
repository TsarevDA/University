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
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.config.SpringTestConfig;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql("/studentData.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private StudentDao studentDao;

	@Test
	void givenStudent_whenCreate_thenCreated() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = Student.builder().firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();

		studentDao.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {

		studentDao.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", "id = 1 ");
		assertEquals(0, actual);
	}

	@Test
	void givenId_whenGetById_thenStudentFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();

		Student actual = studentDao.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudents_whenGetAll_thenStudentsListFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(2).firstName("Petr").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1992-05-03", formatter)).email("mail11111@mail.ru").phone("880899908080")
				.address("Petrov street, 25-5").build();
		List<Student> expected = new ArrayList<>();
		expected.add(student1);
		expected.add(student2);

		List<Student> actual = studentDao.getAll();

		assertEquals(expected, actual);
	}
}
