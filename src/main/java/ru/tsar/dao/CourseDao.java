package ru.tsar.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import ru.tsar.university.model.Course;

public class CourseDao {
	final private String ADD_COURSE_QUERY = "INSERT INTO courses(course_name,course_description) VALUES(?,?)";
	final private String DELETE_COURSE_QUERY = "DELETE FROM courses where course_id =?";
	private JdbcTemplate jdbcTemplate;

	public CourseDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void addCourse(Course course) {
		jdbcTemplate.update(ADD_COURSE_QUERY, course.getName(), course.getDescription());
	}

	public void deleteCourse(int id) {
		jdbcTemplate.update(DELETE_COURSE_QUERY, id);
	}
}
