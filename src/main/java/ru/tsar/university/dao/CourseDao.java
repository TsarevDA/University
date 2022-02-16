package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.dao.mapper.CourseRowMapper;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;

@Component
public class CourseDao {

	private static final Logger LOG = LoggerFactory.getLogger(CourseDao.class);

	private static final String CREATE_COURSE_QUERY = "INSERT INTO courses(name, description) VALUES(?,?)";
	private static final String DELETE_COURSE_QUERY = "DELETE FROM courses WHERE id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM courses WHERE id=?";
	private static final String GET_COURSES_BY_TEACHER_ID_QUERY = "SELECT * FROM teachers_courses tc left join courses c on tc.course_id = c.id WHERE teacher_id=?";
	private static final String GET_COURSES_BY_TEACHER_ID_WITH_LIMIT_QUERY = "SELECT * FROM teachers_courses tc left join courses c on tc.course_id = c.id WHERE teacher_id=? LIMIT ? OFFSET ?";
	private static final String UPDATE_COURSE_QUERY = "UPDATE courses SET name=?, description=? WHERE id=?";
	private static final String GET_COUNT_COURSES_QUERY = "SELECT count(id) FROM courses ";
	private static final String GET_ALL_PAGEABLE_QUERY = "SELECT * FROM courses LIMIT ? OFFSET ?";
	private static final String GET_ALL_QUERY = "SELECT * FROM courses";
	private static final String GET_BY_NAME_QUERY = "SELECT * FROM courses WHERE name = ?";
	private static final String GET_COUNT_TEACHER_COURSES_QUERY = "SELECT count(course_id) FROM teachers_courses WHERE teacher_id=?";

	private JdbcTemplate jdbcTemplate;
	private CourseRowMapper rowMapper;

	public CourseDao(JdbcTemplate jdbcTemplate, CourseRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public void create(Course course) {
		LOG.debug("Created course {}", course);
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
		LOG.debug("Deleted course,id = {}", id);
		jdbcTemplate.update(DELETE_COURSE_QUERY, id);
	}

	public Course getById(int id) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("Course not found by id = {}", id);
			return null;
		}
	}

	public List<Course> getByTeacherId(int id) {
		return jdbcTemplate.query(GET_COURSES_BY_TEACHER_ID_QUERY, rowMapper, id);
	}

	public void update(Course course) {
		LOG.debug("Updated to course: {} ", course);
		jdbcTemplate.update(UPDATE_COURSE_QUERY, course.getName(), course.getDescription(), course.getId());
	}

	public Page<Course> getAll(Pageable pageable) {
		int total = jdbcTemplate.queryForObject(GET_COUNT_COURSES_QUERY, Integer.class);
		List<Course> courses = jdbcTemplate.query(GET_ALL_PAGEABLE_QUERY, rowMapper, pageable.getPageSize(),
				pageable.getOffset());
		return new PageImpl<>(courses, pageable, total);
	}

	public List<Course> getAll() {
		return jdbcTemplate.query(GET_ALL_QUERY, rowMapper);
	}

	public Course getByName(Course course) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_NAME_QUERY, rowMapper, course.getName());
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("Course not found by name = {}", course.getName());
			return null;
		}
	}
}
