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
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql("/studentData.sql")
class StudentDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private StudentDao studentDao;

	@Test
	@DirtiesContext
	void givenStudent_whenCreate_thenCreated() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = new Student.StudentBuilder().setFirstName("Ivan").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1990-01-01", formatter))
				.setEmail("mail@mail.ru").setPhone("88008080").setAddress("Ivanov street, 25-5").build();
		
		studentDao.create(expected);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	@DirtiesContext
	void givenId_whenDeleteById_thenDeleted() {

		studentDao.deleteById(1);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", "id = 1 ");
		assertEquals(0, actual);
	}

	@Test
	@DirtiesContext
	void givenId_whenGetById_thenStudentFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = new Student.StudentBuilder().setId(1).setFirstName("Ivan").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1990-01-01", formatter))
				.setEmail("mail@mail.ru").setPhone("88008080").setAddress("Ivanov street, 25-5").build();
		
		Student actual = studentDao.getById(1);
		
		assertEquals(expected, actual);
	}

	@Test
	@DirtiesContext
	void givenStudents_whenGetAll_thenStudentsListFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student student1 = new Student.StudentBuilder().setId(1).setFirstName("Ivan").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1990-01-01", formatter))
				.setEmail("mail@mail.ru").setPhone("88008080").setAddress("Ivanov street, 25-5").build();
		Student student2 = new Student.StudentBuilder().setId(2).setFirstName("Petr").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1992-05-03", formatter))
				.setEmail("mail11111@mail.ru").setPhone("880899908080").setAddress("Petrov street, 25-5").build();
		List<Student> expected = new ArrayList<>();
		expected.add(student1);
		expected.add(student2);

		List<Student> actual = studentDao.getAll();
		
		assertEquals(expected, actual);
	}
}
