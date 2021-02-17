package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.SpringTestConfig;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;

class CourseDaoTest {

	final static private String GET_COURSES_QUERY = "SELECT * FROM courses";
	final static private String GET_COUNT_BY_ID_QUERY = "select count(*) FROM courses WHERE id=?";
	final static private String CREATE_COURSE_QUERY = "INSERT INTO courses(name,description) VALUES(?,?)";

	private AnnotationConfigApplicationContext context;
	private JdbcTemplate jdbcTemplate;
	private CourseDao courseDao;

	@BeforeEach
	void setUp() {
		context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		context.getBean("databasePopulator", DatabasePopulator.class);
		this.jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		this.courseDao = context.getBean("courseDao", CourseDao.class);
	}

	@AfterEach
	void setDown() {
		context.close();
	}

	@Test
	void setCourse_whenCreate_thenCreateCourse() {

		Course expected = new Course("Astronomy", "Science about stars and deep space");
		courseDao.create(expected);
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteCourse() {
		Course course = new Course("Astronomy", "Science about stars and deep space");
		courseDao.create(course);
		courseDao.deleteById(course.getId());
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", "id = " + course.getId());
		assertEquals(0, actual);
	}

	@Test
	void setId_whenGetById_thenGetCourse() {
		Course expected = new Course("Astronomy", "Science about stars and deep space");
		courseDao.create(expected);
		Course actual = courseDao.getById(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	void setCourses_whenGetAll_thenCoursesList() {
		Course first = new Course("Astronomy", "Science about stars and deep space");
		courseDao.create(first);

		Course second = new Course("Math", "Science about numbers");
		courseDao.create(second);

		List<Course> expected = new ArrayList<>();
		expected.add(first);
		expected.add(second);

		List<Course> actual = courseDao.getAll();
		assertEquals(expected, actual);
	}
}
