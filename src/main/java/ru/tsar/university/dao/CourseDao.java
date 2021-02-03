package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.mapper.CourseRowMapper;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Teacher;

@Component
public class CourseDao {

	final static private String CREATE_COURSE_QUERY = "INSERT INTO courses(name,description) VALUES(?,?)";
	final static private String DELETE_COURSE_QUERY = "DELETE FROM courses WHERE id =?";
	final static private String GET_BY_ID_QUERY = "SELECT * FROM courses WHERE id=?";
	final private static String GET_COURSES_BY_TEACHER_ID_QUERY = "SELECT * FROM teachers_courses tc left join courses c on tc.teacher_id = c.id WHERE teacher_id=?";

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
		CourseRowMapper rowMapper = new CourseRowMapper();
		return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
	}

	public List<Course> getCoursesByTeacher(Teacher teacher) {
		CourseRowMapper rowMapper = new CourseRowMapper();
		List<Course> courses = jdbcTemplate.query(GET_COURSES_BY_TEACHER_ID_QUERY, rowMapper, teacher.getId());
		return courses;
	}
}
