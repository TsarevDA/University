package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tsar.university.dao.StudentDaoTest.TestData.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
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
import ru.tsar.university.model.Student;

@SpringJUnitConfig(SpringTestConfig.class)
@Sql("/studentData.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@WebAppConfiguration
class StudentDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private StudentDao studentDao;

	@Test
	void givenStudent_whenCreate_thenCreated() {
		Student expected = Student.builder().firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1)).email("mail@mail.ru").phone("88008080")
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
		Student expected = student_1;

		Student actual = studentDao.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudents_whenGetAll_thenStudentsListFound() {
		List<Student> students = new ArrayList<>();
		students.add(student_1);
		students.add(student_2);
		Page<Student> expected = new PageImpl<>(students, pageable, students.size());
		Page<Student> actual = studentDao.getAll(pageable);

		assertEquals(expected, actual);
	}

	interface TestData {
		Pageable pageable = PageRequest.of(0, 5);
		Student student_1 = Student.builder()
				.id(1)
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.build();
		Student student_2 = Student.builder()
				.id(2)
				.firstName("Petr")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1992, Month.MAY, 3))
				.email("mail11111@mail.ru")
				.phone("880899908080")
				.address("Petrov street, 25-5")
				.build();
	}
}
