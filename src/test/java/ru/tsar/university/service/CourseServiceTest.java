package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

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

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql("/courseData.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseServiceTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CourseService courseService;

	@Test
	void givenNewCourse_whenCreate_thenCreated() {
		Course expected = Course.builder().name("Biology").description("Science about plants").build();

		courseService.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {
		
		courseService.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	void givenId_whenGetById_thenCourseFound() {
		Course expected = Course.builder().id(1).name("Astronomy").description("Science about stars and deep space")
				.build();

		Course actual = courseService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenCourses_whenGetAll_thenCoursesListFound() {
		Course course1 = Course.builder().id(1).name("Astronomy").description("Science about stars and deep space")
				.build();
		Course course2 = Course.builder().id(2).name("Math").description("Science about numbers").build();
		List<Course> expected = new ArrayList<>();
		expected.add(course1);
		expected.add(course2);

		List<Course> actual = courseService.getAll();

		assertEquals(expected, actual);
	}
}
