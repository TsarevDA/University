package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tsar.university.dao.CourseDaoTest.TestData.*;

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
import ru.tsar.university.model.Course;

@SpringJUnitConfig(SpringTestConfig.class)
@Sql("/courseData.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@WebAppConfiguration
class CourseDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CourseDao courseDao;

	@Test
	void givenNewCourse_whenCreate_thenCreated() {
		Course expected = Course.builder().name("Astronomy").description("Science about stars and deep space").build();

		courseDao.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {

		courseDao.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	void givenId_whenGetById_thenCourseFound() {
		Course expected = course_1;

		Course actual = courseDao.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenCourses_whenGetAll_thenCoursesListFound() {
		List<Course> courses = new ArrayList<>();
		courses.add(course_1);
		courses.add(course_2);
		Page<Course> expected = new PageImpl<>(courses, pageable, courses.size());
		Page<Course> actual = courseDao.getAll(pageable);

		assertEquals(expected, actual);
	}
	
	interface TestData {
		Pageable pageable = PageRequest.of(0, 5);
		Course course_1 = Course.builder()
				.id(1)
				.name("Astronomy")
				.description("Science about stars and deep space")
				.build();
		Course course_2 = Course.builder()
				.id(2)
				.name("Math")
				.description("Science about numbers")
				.build();	
	}
}
