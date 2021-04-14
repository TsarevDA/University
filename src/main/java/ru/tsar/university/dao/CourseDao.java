package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.dao.mapper.CourseRowMapper;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;

@Component
public class CourseDao {

	private static final String CREATE_COURSE_QUERY = "INSERT INTO courses(name, description) VALUES(?,?)";
	private static final String DELETE_COURSE_QUERY = "DELETE FROM courses WHERE id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM courses WHERE id=?";
	private static final String GET_COURSES_BY_TEACHER_ID_QUERY = "SELECT * FROM teachers_courses tc left join courses c on tc.teacher_id = c.id WHERE teacher_id=?";
	private static final String UPDATE_COURSE_QUERY = "UPDATE courses SET name=?, description=? WHERE id=?";
	private static final String GET_ALL_QUERY = "SELECT * FROM courses ";
	private static final String GET_BY_NAME_QUERY = "SELECT * FROM courses WHERE name = ?";

	private JdbcTemplate jdbcTemplate;
	private CourseRowMapper rowMapper;

	public CourseDao(JdbcTemplate jdbcTemplate, CourseRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
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
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Course> getByTeacherId(int id) {
		return jdbcTemplate.query(GET_COURSES_BY_TEACHER_ID_QUERY, rowMapper, id);
	}

	public void update(Course course) {
		jdbcTemplate.update(UPDATE_COURSE_QUERY, course.getName(), course.getDescription(), course.getId());
	}

	public List<Course> getAll() {
		return jdbcTemplate.query(GET_ALL_QUERY, rowMapper);
	}

	public Course getByName(Course course) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_NAME_QUERY, rowMapper, course.getName());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
