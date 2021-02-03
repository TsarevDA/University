package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.mapper.TeacherRowMapper;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Teacher;

@Component
public class TeacherDao {

	final static private String CREATE_TEACHER_QUERY = "INSERT INTO teachers(first_name,last_name,gender,birth_date,email,phone,address) VALUES(?,?,?,?,?,?,?)";
	final static private String CREATE_TEACHERS_COURSES_QUERY = "INSERT INTO teachers_courses(teacher_id,course_id) VALUES(?,?)";
	final static private String DELETE_TEACHERS_COURSES_QUERY = "DELETE FROM teachers_courses where teacher_id = ?";
	final static private String DELETE_TEACHER_QUERY = "DELETE FROM teachers where id =?";
	final static private String GET_BY_ID_QUERY = "SELECT * FROM teachers WHERE id=?";
	final static private String GET_COURSES_BY_TEACHER_ID_QUERY = "SELECT * FROM teachers_courses WHERE teacher_id = ?";

	private JdbcTemplate jdbcTemplate;

	public TeacherDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void create(Teacher teacher) {

		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_TEACHER_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, teacher.getFirstName());
			ps.setString(2, teacher.getLastName());
			ps.setString(3, teacher.getGender().name());
			ps.setDate(4, java.sql.Date.valueOf(teacher.getBirthDate()));
			ps.setString(5, teacher.getEmail());
			ps.setString(6, teacher.getPhone());
			ps.setString(7, teacher.getAddress());
			return ps;
		}, holder);
		teacher.setId((int) holder.getKeys().get("id"));

		teacher.getCourses().stream()
				.forEach(c -> jdbcTemplate.update(CREATE_TEACHERS_COURSES_QUERY, teacher.getId(), c.getId()));

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_TEACHERS_COURSES_QUERY, id);
		jdbcTemplate.update(DELETE_TEACHER_QUERY, id);
	}

	public Teacher getById(int id) {
		TeacherRowMapper rowMapper = new TeacherRowMapper();
		Teacher teacher = jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
		CourseDao courseDao = new CourseDao(jdbcTemplate);
		teacher.setCourses(courseDao.getCoursesByTeacher(teacher));
		return teacher;

	}
}
