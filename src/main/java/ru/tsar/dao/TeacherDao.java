package ru.tsar.dao;

import org.springframework.jdbc.core.JdbcTemplate;


import ru.tsar.university.model.Teacher;

public class TeacherDao {

	final private String ADD_TEACHER_QUERY = "INSERT INTO teachers(first_name,last_name,gender,birth_date,email,phone,address) VALUES(?,?,?,?,?,?,?)";
	final private String ADD_TEACHERS_COURSES_QUERY = "INSERT INTO teachers_courses(group_id,student_id) VALUES(?,?)";
	final private String DELETE_TEACHERS_COURSES_QUERY = "DELETE FROM teachers_courses where group_id =?";
	final private String DELETE_TEACHER_QUERY = "DELETE FROM teachers where id =?";
	private JdbcTemplate jdbcTemplate;

	public TeacherDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void addTeacher(Teacher teacher) {
		jdbcTemplate.update(ADD_TEACHER_QUERY, teacher.getFirstName(), teacher.getLastName(), teacher.getGender().name(),
				teacher.getBirthDate(), teacher.getEmail(), teacher.getPhone(), teacher.getAddress());
		//teacher.getCourses().stream()
		//.forEach(s -> jdbcTemplate.update(ADD_TEACHERS_COURSES_QUERY, teacher.getId(), s.getId()));
	}

	public void deleteTeacher(int id) {
		jdbcTemplate.update(DELETE_TEACHERS_COURSES_QUERY, id);
		jdbcTemplate.update(DELETE_TEACHER_QUERY, id);
	}

}
