package ru.tsar.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import ru.tsar.university.model.Student;

public class StudentDao {

	final private String ADD_STUDENT_QUERY = "INSERT INTO students(first_name,last_name,gender,birth_date,email,phone,address) VALUES(?,?,?,?,?,?,?)";
	final private String DELETE_STUDENT_QUERY = "DELETE FROM students where id =?";
	private JdbcTemplate jdbcTemplate;

	public StudentDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void addStudent(Student student) {
		jdbcTemplate.update(ADD_STUDENT_QUERY, student.getFirstName(), student.getLastName(),
				student.getGender().name(), student.getBirthDate(), student.getEmail(), student.getPhone(),
				student.getAddress());
	}

	public void deleteStudent(int id) {
		jdbcTemplate.update(DELETE_STUDENT_QUERY, id);
	}

}
