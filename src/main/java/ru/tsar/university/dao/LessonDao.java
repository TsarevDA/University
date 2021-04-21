package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.tsar.university.dao.mapper.LessonRowMapper;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Teacher;

@Component
public class LessonDao {

	private static final String CREATE_LESSON_QUERY = "INSERT INTO lessons(course_id, teacher_id, day, lesson_time_id, auditorium_id) VALUES(?,?,?,?,?)";
	private static final String CREATE_LESSONS_GROUPS_QUERY = "INSERT INTO lessons_groups(lesson_id, group_id) VALUES(?,?)";
	private static final String DELETE_LESSONS_GROUPS_QUERY = "DELETE FROM lessons_groups where lesson_id =?";
	private static final String DELETE_LESSON_QUERY = "DELETE FROM lessons where id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM lessons WHERE id =?";
	private static final String UPDATE_LESSON_QUERY = "UPDATE lessons SET course_id=?,teacher_id=?,day=?,lesson_time_id=?,auditorium_id=? WHERE id=?";
	private static final String GET_ALL_QUERY = "SELECT * FROM lessons";
	private static final String GET_BY_DAY_TIME_QUERY = "SELECT * FROM lessons WHERE day=? AND lesson_time_id = ? ";
	private static final String GET_BY_DAY_TIME_AUDITORIUM_QUERY = "SELECT * FROM lessons WHERE day=? AND lesson_time_id = ? AND auditorium_id = ?";
	private static final String GET_BY_DAY_TIME_TEACHER_QUERY = "SELECT * FROM lessons WHERE day=? AND lesson_time_id = ? AND teacher_id = ?";
	
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

		lesson.getGroups().stream()
				.forEach(g -> jdbcTemplate.update(CREATE_LESSONS_GROUPS_QUERY, lesson.getId(), g.getId()));
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_LESSONS_GROUPS_QUERY, id);
		jdbcTemplate.update(DELETE_LESSON_QUERY, id);
	}

	public Lesson getById(int id) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void update(Lesson lesson) {
		jdbcTemplate.update(UPDATE_LESSON_QUERY, lesson.getCourse().getId(), lesson.getTeacher().getId(),
				lesson.getDay(), lesson.getTime().getOrderNumber(), lesson.getAuditorium().getId(), lesson.getId());
		jdbcTemplate.update(DELETE_LESSONS_GROUPS_QUERY, lesson.getId());
		lesson.getGroups().stream()
				.forEach(g -> jdbcTemplate.update(CREATE_LESSONS_GROUPS_QUERY, lesson.getId(), g.getId()));

	}

	public List<Lesson> getAll() {
		return jdbcTemplate.query(GET_ALL_QUERY, rowMapper);
	}

	public List<Lesson> getByDayTime(LocalDate day, LessonTime lessonTime) {
		return jdbcTemplate.query(GET_BY_DAY_TIME_QUERY, rowMapper, day, lessonTime.getId());
	}
	
	public List<Lesson> getByDayTimeAuditorium(LocalDate day, LessonTime lessonTime, Auditorium auditorium) {
		return jdbcTemplate.query(GET_BY_DAY_TIME_AUDITORIUM_QUERY, rowMapper,  day, lessonTime.getId(), auditorium.getId());
	}

	public List<Lesson> getByDayTimeTeacher(LocalDate day, LessonTime lessonTime, Teacher teacher) {
		return jdbcTemplate.query(GET_BY_DAY_TIME_TEACHER_QUERY, rowMapper,  day, lessonTime.getId(), teacher.getId());
	}
}
