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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.tsar.university.dao.mapper.TeacherRowMapper;
import ru.tsar.university.model.Teacher;

@Component
public class TeacherDao {

	private static final Logger LOG = LoggerFactory.getLogger(TeacherDao.class);

	private static final String CREATE_TEACHER_QUERY = "INSERT INTO teachers(first_name, last_name, gender, birth_date, email, phone, address) VALUES(?,?,?,?,?,?,?)";
	private static final String CREATE_TEACHERS_COURSES_QUERY = "INSERT INTO teachers_courses(teacher_id, course_id) VALUES(?,?)";
	private static final String DELETE_TEACHERS_COURSES_QUERY = "DELETE FROM teachers_courses where teacher_id = ?";
	private static final String DELETE_TEACHER_QUERY = "DELETE FROM teachers where id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM teachers WHERE id=?";
	private static final String UPDATE_TEACHER_QUERY = "UPDATE teachers SET first_name=?, last_name=?, gender=?, birth_date=?, email=?, phone=?, address=? WHERE id=?";
	private static final String GET_ALL_PAGEABLE_QUERY = "SELECT * FROM teachers LIMIT ? OFFSET ?";
	private static final String GET_ALL_QUERY = "SELECT * FROM teachers";
	private static final String GET_COUNT_TEACHERS_QUERY = "SELECT count(id) FROM teachers";

	private JdbcTemplate jdbcTemplate;
	private TeacherRowMapper rowMapper;

	public TeacherDao(JdbcTemplate jdbcTemplate, TeacherRowMapper rowMapper, CourseDao courseDao) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void create(Teacher teacher) {
		LOG.debug("Creqated teacher: {}", teacher);
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

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void deleteById(int id) {
		LOG.debug("Deleted teacher, id = {}", id);
		jdbcTemplate.update(DELETE_TEACHERS_COURSES_QUERY, id);
		jdbcTemplate.update(DELETE_TEACHER_QUERY, id);
	}

	public Teacher getById(int id) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("Teacher not found by id = {}", id);
			return null;
		}
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void update(Teacher teacher) {
		LOG.debug("Call update {}", teacher);
		jdbcTemplate.update(UPDATE_TEACHER_QUERY, teacher.getFirstName(), teacher.getLastName(),
				teacher.getGender().name(), java.sql.Date.valueOf(teacher.getBirthDate()), teacher.getEmail(),
				teacher.getPhone(), teacher.getAddress(), teacher.getId());
		jdbcTemplate.update(DELETE_TEACHERS_COURSES_QUERY, teacher.getId());
		teacher.getCourses().stream()
				.forEach(c -> jdbcTemplate.update(CREATE_TEACHERS_COURSES_QUERY, teacher.getId(), c.getId()));
	}

	public Page<Teacher> getAll(Pageable pageable) {
		int total = jdbcTemplate.queryForObject(GET_COUNT_TEACHERS_QUERY, Integer.class);
		List<Teacher> teachers = jdbcTemplate.query(GET_ALL_PAGEABLE_QUERY, rowMapper, pageable.getPageSize(),
				pageable.getOffset());
		return new PageImpl<>(teachers, pageable, total);
	}

	public List<Teacher> getAll() {
		return jdbcTemplate.query(GET_ALL_QUERY, rowMapper);
	}
}
