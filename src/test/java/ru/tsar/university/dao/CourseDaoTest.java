package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ru.tsar.university.SpringConfig;

import ru.tsar.university.model.Course;

@SpringJUnitConfig(classes = SpringConfig.class)
@Sql("/schema.sql")
class CourseDaoTest {

	final static private String GET_COURSES_QUERY = "SELECT * FROM courses";
	final static private String GET_COUNT_BY_ID_QUERY = "select count(*) FROM courses WHERE id=?";
	final static private String CREATE_COURSE_QUERY = "INSERT INTO courses(name,description) VALUES(?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CourseDao courseDao;

	@Test
	void setCourse_whenCreate_thenCreateCourse() {
		Course expected = new Course("Astronomy", "Science about stars and deep space");
		courseDao.create(expected);
		List<Course> courses = jdbcTemplate.query(GET_COURSES_QUERY, (resultSet, rowNum) -> {
			Course newCourse = new Course(resultSet.getInt("id"), resultSet.getString("name"),
					resultSet.getString("description"));
			return newCourse;
		});
		Course actual = courses.get(courses.size() - 1);
		assertEquals(expected, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteCourse() {
		Course course = new Course("Astronomy", "Science about stars and deep space");
		courseDao.create(course);
		courseDao.deleteById(course.getId());

		int actual = jdbcTemplate.queryForObject(GET_COUNT_BY_ID_QUERY, Integer.class, course.getId());
		assertEquals(0, actual);
	}

	@Test
	void setId_whenGetById_thenGetCourse() {
		Course expected = new Course("Astronomy", "Science about stars and deep space");
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_COURSE_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, expected.getName());
			ps.setString(2, expected.getDescription());
			return ps;
		}, holder);
		expected.setId((int) holder.getKeys().get("id"));
		Course actual = courseDao.getById(expected.getId());
		assertEquals(expected, actual);
	}
}
