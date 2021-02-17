package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.SpringConfig;
import ru.tsar.university.SpringTestConfig;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;

class StudentDaoTest {

	final static private String GET_STUDENT_REQUEST = "SELECT * FROM students";
	final static private String GET_COUNT_BY_ID_REQUEST = "select count(*) FROM students WHERE id=?";
	final static private String CREATE_STUDENT_QUERY = "INSERT INTO students(first_name, last_name, gender, birth_date, email, phone, address) VALUES(?,?,?,?,?,?,?)";

	private AnnotationConfigApplicationContext context;
	private JdbcTemplate jdbcTemplate;
	private StudentDao studentDao;

	@BeforeEach
	void setUp() {
		context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		context.getBean("databasePopulator", DatabasePopulator.class);
		this.jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		this.studentDao = context.getBean("studentDao", StudentDao.class);
	}

	@AfterEach
	void setDown() {
		context.close();
	}

	@Test
	void setStudent_whenCreate_thenCreateStudent() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = new Student("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		studentDao.create(expected);
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteStudent() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student student = new Student("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		studentDao.create(student);
		studentDao.deleteById(student.getId());

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", "id = " + student.getId());
		assertEquals(0, actual);
	}

	@Test
	void setId_whenGetById_thenGetStudent() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = new Student("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		studentDao.create(expected);
		Student actual = studentDao.getById(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	void setStudents_whenGetAll_thenStudentsList() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student first = new Student("Ivan", "Ivanov", Gender.valueOf("MALE"), LocalDate.parse("1990-01-01", formatter),
				"mail@mail.ru", "88008080", "Ivanov street, 25-5");
		studentDao.create(first);

		Student second = new Student("Petr", "Ivanov", Gender.valueOf("MALE"), LocalDate.parse("1992-05-03", formatter),
				"mail11111@mail.ru", "880899908080", "Petrov street, 25-5");
		studentDao.create(second);

		List<Student> expected = new ArrayList<>();
		expected.add(first);
		expected.add(second);

		List<Student> actual = studentDao.getAll();
		assertEquals(expected, actual);
	}
}
