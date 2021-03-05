package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

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

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext
@Sql("/courseData.sql")
class CourseDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CourseDao courseDao;

	@Test
	@DirtiesContext
	void givenNewCourse_whenCreate_thenCreated() {
		Course expected = new Course.CourseBuilder().setName("Astronomy").setDescription("Science about stars and deep space")
				.build();
		
		courseDao.create(expected);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	@DirtiesContext
	void givenId_whenDeleteById_thenDeleted() {
		
		courseDao.deleteById(1);
		
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	@DirtiesContext
	void givenId_whenGetById_thenCourseFound() {
		Course expected = new Course.CourseBuilder().setId(1).setName("Astronomy")
				.setDescription("Science about stars and deep space").build();
		
		Course actual = courseDao.getById(1);
		
		assertEquals(expected, actual);
	}

	@Test
	@DirtiesContext
	void givenCourses_whenGetAll_thenCoursesListFound() {
		Course course1 = new Course.CourseBuilder().setId(1).setName("Astronomy")
				.setDescription("Science about stars and deep space").build();
		Course course2 = new Course.CourseBuilder().setId(2).setName("Math").setDescription("Science about numbers").build();
		List<Course> expected = new ArrayList<>();
		expected.add(course1);
		expected.add(course2);
		
		List<Course> actual = courseDao.getAll();
		
		assertEquals(expected, actual);
	}
}
