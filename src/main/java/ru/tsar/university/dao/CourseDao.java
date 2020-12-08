package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Course;

@Component
public class CourseDao {
	final static private String CREATE_COURSE_QUERY = "INSERT INTO courses(course_name,description) VALUES(?,?)";
	final static private String DELETE_COURSE_QUERY = "DELETE FROM courses WHERE id =?";
	final static private String GET_BY_ID_REQUEST = "SELECT c.* FROM courses c WHERE id=?";

	private JdbcTemplate jdbcTemplate;

	public CourseDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void create(Course course) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_COURSE_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, course.getName());
			ps.setString(2, course.getDescription());
			return ps;
		}, holder);
		course.setId((int) holder.getKeys().get("id"));
	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_COURSE_QUERY, id);
	}

	public Course getById(int id) {
		Course course = jdbcTemplate.queryForObject(GET_BY_ID_REQUEST, (resultSet, rowNum) -> {
			Course newCourse = new Course(id, resultSet.getString("course_name"), resultSet.getString("description"));
			return newCourse;
		}, id);
		return course;
	}
}
