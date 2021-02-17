package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.SpringConfig;
import ru.tsar.university.SpringTestConfig;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;

class TeacherDaoTest {

	final static private String GET_TEACHER_REQUEST = "SELECT t.* FROM teachers t";
	final static private String GET_COUNT_BY_ID_REQUEST = "select count(*) FROM teachers WHERE id=?";
	final static private String CREATE_TEACHER_QUERY = "INSERT INTO teachers(first_name, last_name, gender, birth_date, email, phone, address) VALUES(?,?,?,?,?,?,?)";

	private AnnotationConfigApplicationContext context;
	private JdbcTemplate jdbcTemplate;
	private TeacherDao teacherDao;

	@BeforeEach
	void setUp() {
		context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		context.getBean("databasePopulator", DatabasePopulator.class);
		this.jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		this.teacherDao = context.getBean("teacherDao", TeacherDao.class);
	}

	@AfterEach
	void setDown() {
		context.close();
	}

	@Test
	void setTeacher_whenCreate_thenCreateTeacher() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher expected = new Teacher("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		teacherDao.create(expected);
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteTeacher() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher teacher = new Teacher("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		teacherDao.create(teacher);
		teacherDao.deleteById(teacher.getId());

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", "id = " + teacher.getId());
		assertEquals(0, actual);
	}

	@Test
	void setId_whenGetById_thenGetTeacher() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<Course> courses = new ArrayList<>();
		Teacher expected = new Teacher("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		expected.setCourses(courses);
		teacherDao.create(expected);
		Teacher actual = teacherDao.getById(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	void setTeachers_whenGetAll_theneachersList() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<Course> courses = new ArrayList<>();
		Teacher first = new Teacher("Ivan", "Ivanov", Gender.valueOf("MALE"), LocalDate.parse("1990-01-01", formatter),
				"mail@mail.ru", "88008080", "Ivanov street, 25-5");
		first.setCourses(courses);
		teacherDao.create(first);

		Teacher second = new Teacher("Petr", "Ivanov", Gender.valueOf("MALE"), LocalDate.parse("1992-05-03", formatter),
				"mail11111@mail.ru", "880899908080", "Petrov street, 25-5");
		second.setCourses(courses);
		teacherDao.create(second);

		List<Teacher> expected = new ArrayList<>();
		expected.add(first);
		expected.add(second);

		List<Teacher> actual = teacherDao.getAll();
		assertEquals(expected, actual);
	}

}
