package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.tsar.university.dao.mapper.LessonRowMapper;
import ru.tsar.university.model.Lesson;

@Component
public class LessonDao {

	private static final String CREATE_LESSON_QUERY = "INSERT INTO lessons(course_id, teacher_id, day, lesson_time_id, auditorium_id) VALUES(?,?,?,?,?)";
	private static final String CREATE_LESSONS_GROUPS_QUERY = "INSERT INTO lessons_groups(lesson_id, group_id) VALUES(?,?)";
	private static final String DELETE_LESSONS_GROUPS_QUERY = "DELETE FROM lessons_groups where lesson_id =?";
	private static final String DELETE_LESSON_QUERY = "DELETE FROM lessons where id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM lessons WHERE id =?";
	private static final String UPDATE_LESSON_QUERY = "UPDATE lessons SET course_id=?,teacher_id=?,day=?,lesson_time_id=?,auditorium_id=? WHERE id=?";
	private static final String UPDATE_LESSONS_GROUPS_QUERY = "UPDATE lessons_groups SET group_id=? WHERE lesson_id=?";
	private static final String GET_ALL_QUERY = "SELECT * FROM lessons";

	private JdbcTemplate jdbcTemplate;
	private LessonRowMapper rowMapper;

	public LessonDao(JdbcTemplate jdbcTemplate, LessonRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void create(Lesson lesson) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_LESSON_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, lesson.getCourse().getId());
			ps.setInt(2, lesson.getTeacher().getId());
			ps.setDate(3, java.sql.Date.valueOf(lesson.getDay()));
			ps.setInt(4, lesson.getTime().getId());
			ps.setInt(5, lesson.getAuditorium().getId());
			return ps;
		}, holder);

		lesson.setId((int) holder.getKeys().get("id"));

		lesson.getGroup().stream()
				.forEach(g -> jdbcTemplate.update(CREATE_LESSONS_GROUPS_QUERY, lesson.getId(), g.getId()));
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_LESSONS_GROUPS_QUERY, id);
		jdbcTemplate.update(DELETE_LESSON_QUERY, id);
	}

	public Lesson getById(int id) {
		return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void update(Lesson lesson) {
		jdbcTemplate.update(UPDATE_LESSON_QUERY, lesson.getCourse().getId(), lesson.getTeacher().getId(),
				lesson.getDay(), lesson.getTime().getOrderNumber(), lesson.getAuditorium().getId(), lesson.getId());
		jdbcTemplate.update(DELETE_LESSONS_GROUPS_QUERY, lesson.getId());
		lesson.getGroup().stream()
				.forEach(g -> jdbcTemplate.update(CREATE_LESSONS_GROUPS_QUERY, lesson.getId(), g.getId()));

	}

	public List<Lesson> getAll() {
		return jdbcTemplate.query(GET_ALL_QUERY, rowMapper);
	}

}
