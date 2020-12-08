package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Teacher;

@Component
public class TeacherDao {

	final static private String CREATE_TEACHER_QUERY = "INSERT INTO teachers(first_name,last_name,gender,birth_date,email,phone,address) VALUES(?,?,?,?,?,?,?)";
	final static private String CREATE_TEACHERS_COURSES_QUERY = "INSERT INTO teachers_courses(teacher_id,course_id) VALUES(?,?)";
	final static private String DELETE_TEACHERS_COURSES_QUERY = "DELETE FROM teachers_courses where teacher_id = ?";
	final static private String DELETE_TEACHER_QUERY = "DELETE FROM teachers where id =?";
	final static private String GET_BY_ID_REQUEST = "SELECT t.* FROM teachers t WHERE id=?";
	final static private String GET_COURSES_BY_TEACHER_ID_REQUEST = "SELECT tc.* FROM teachers_courses tc WHERE teacher_id = ?";

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
		Teacher teacher = jdbcTemplate.queryForObject(GET_BY_ID_REQUEST, (resultSet, rowNum) -> {
			Teacher newTeacher = new Teacher(id, resultSet.getString("first_name"), resultSet.getString("last_name"),
					Gender.valueOf(resultSet.getString("gender")), resultSet.getDate("birth_date").toLocalDate(),
					resultSet.getString("email"), resultSet.getString("phone"), resultSet.getString("address"));
			return newTeacher;
		}, id);
		List<Integer> coursesId = jdbcTemplate.query(GET_COURSES_BY_TEACHER_ID_REQUEST, (resultSet, rowNum) -> {
			return resultSet.getInt("course_id");
		}, id);
		CourseDao courseDao = new CourseDao(jdbcTemplate);
		List<Course> courses = new ArrayList<>();
		coursesId.stream().forEach(cId -> {
			courses.add(courseDao.getById(cId));
		});
		teacher.setCourses(courses);

		return teacher;
	}

}
