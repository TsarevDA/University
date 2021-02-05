package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.mapper.StudentRowMapper;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;

@Component
public class StudentDao {

	private static final String ADD_STUDENT_QUERY = "INSERT INTO students(first_name,last_name,gender,birth_date,email,phone,address) VALUES(?,?,?,?,?,?,?)";
	private static final String DELETE_STUDENT_QUERY = "DELETE FROM students where id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM students WHERE id=?";
	private static final String GET_STUDENTS_BY_GROUP_ID_QUERY = "SELECT * FROM groups_students gs left join students s on gs.student_id = s.id WHERE group_id=?";
	private static final String UPDATE_STUDENT_QUERY = "UPDATE students SET first_name=?,last_name=?,gender=?,birth_date=?,email=?,phone=?,address=? WHERE id=?";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private StudentRowMapper rowMapper;

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
		return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
	}

	public List<Student> getByGroup(Group group) {
		return jdbcTemplate.query(GET_STUDENTS_BY_GROUP_ID_QUERY, rowMapper, group.getId());
	}

	public void update(Student student) {
		jdbcTemplate.update(UPDATE_STUDENT_QUERY, student.getFirstName(), student.getLastName(),
				student.getGender().name(), student.getBirthDate(), student.getPhone(), student.getAddress(),
				student.getId());
	}

}
