package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.dao.mapper.StudentRowMapper;
import ru.tsar.university.model.Student;

@Component
public class StudentDao {

	private static final Logger LOG = LoggerFactory.getLogger(StudentDao.class);
	
	private static final String ADD_STUDENT_QUERY = "INSERT INTO students(first_name, last_name, gender, birth_date, email, phone, address) VALUES(?,?,?,?,?,?,?)";
	private static final String DELETE_STUDENT_QUERY = "DELETE FROM students where id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM students WHERE id=?";
	private static final String GET_STUDENTS_BY_GROUP_ID_QUERY = "SELECT s.* FROM groups_students gs left join students s on gs.student_id = s.id WHERE group_id=?";
	private static final String UPDATE_STUDENT_QUERY = "UPDATE students SET first_name=?, last_name=?, gender=?, birth_date=?, email=?, phone=?, address=? WHERE id=?";
	private static final String GET_ALL_QUERY = "SELECT * FROM students ";

	private JdbcTemplate jdbcTemplate;
	private StudentRowMapper rowMapper;

	public StudentDao(JdbcTemplate jdbcTemplate, StudentRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public void create(Student student) {
		LOG.debug("Created student {}", student);
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
		LOG.debug("Deleted student, id = {}",id);
		jdbcTemplate.update(DELETE_STUDENT_QUERY, id);
	}

	public Student getById(int id) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("Student not found by id = {}",id);
			return null;
		}
	}

	public List<Student> getByGroupId(int id) {
		try {
			return jdbcTemplate.query(GET_STUDENTS_BY_GROUP_ID_QUERY, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("Student not found by id = {}",id);
			return null;
		}
	}

	public void update(Student student) {
		LOG.debug("Call update {}", student);
		jdbcTemplate.update(UPDATE_STUDENT_QUERY, student.getFirstName(), student.getLastName(),
				student.getGender().name(), student.getBirthDate(), student.getPhone(), student.getAddress(),
				student.getId());
	}

	public List<Student> getAll() {
		return jdbcTemplate.query(GET_ALL_QUERY, rowMapper);
	}

}
