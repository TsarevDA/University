package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;

@Component
public class StudentDao {

	final static private String ADD_STUDENT_QUERY = "INSERT INTO students(first_name,last_name,gender,birth_date,email,phone,address) VALUES(?,?,?,?,?,?,?)";
	final static private String DELETE_STUDENT_QUERY = "DELETE FROM students where id =?";
	final static private String GET_BY_ID_REQUEST = "SELECT s.* FROM students s WHERE id=?";

	private JdbcTemplate jdbcTemplate;

	public StudentDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void create(Student student) {

		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(ADD_STUDENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, student.getFirstName());
			ps.setString(2, student.getLastName());
			ps.setString(3, student.getGender().name());
			ps.setDate(4, java.sql.Date.valueOf(student.getBirthDate()));
			ps.setString(5, student.getEmail());
			ps.setString(6, student.getPhone());
			ps.setString(7, student.getAddress());
			return ps;
		}, holder);
		student.setId((int) holder.getKeys().get("id"));

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_STUDENT_QUERY, id);
	}

	public Student getById(int id) {
		Student student = jdbcTemplate.queryForObject(GET_BY_ID_REQUEST, (resultSet, rowNum) -> {
			Student newStudent = new Student(id, resultSet.getString("first_name"), resultSet.getString("last_name"),
					Gender.valueOf(resultSet.getString("gender")), resultSet.getDate("birth_date").toLocalDate(),
					resultSet.getString("email"), resultSet.getString("phone"), resultSet.getString("address"));
			return newStudent;
		}, id);
		return student;
	}

}
