package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
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

import ru.tsar.university.dao.mapper.LessonRowMapper;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Teacher;

@Component
public class LessonDao {

	private static final Logger LOG = LoggerFactory.getLogger(LessonDao.class);

	private static final String CREATE_LESSON_QUERY = "INSERT INTO lessons(course_id, teacher_id, day, lesson_time_id, auditorium_id) VALUES(?,?,?,?,?)";
	private static final String CREATE_LESSONS_GROUPS_QUERY = "INSERT INTO lessons_groups(lesson_id, group_id) VALUES(?,?)";
	private static final String DELETE_LESSONS_GROUPS_QUERY = "DELETE FROM lessons_groups where lesson_id =?";
	private static final String DELETE_LESSON_QUERY = "DELETE FROM lessons where id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM lessons WHERE id =?";
	private static final String UPDATE_LESSON_QUERY = "UPDATE lessons SET course_id=?,teacher_id=?,day=?,lesson_time_id=?,auditorium_id=? WHERE id=?";
	private static final String GET_ALL_PAGEABLE_QUERY = "SELECT * FROM lessons LIMIT ? OFFSET ?";
	private static final String GET_ALL_QUERY = "SELECT * FROM lessons";
	private static final String GET_BY_DAY_TIME_QUERY = "SELECT * FROM lessons WHERE day=? AND lesson_time_id = ? ";
	private static final String GET_BY_DAY_TIME_AUDITORIUM_QUERY = "SELECT * FROM lessons WHERE day=? AND lesson_time_id = ? AND auditorium_id = ?";
	private static final String GET_BY_DAY_TIME_TEACHER_QUERY = "SELECT * FROM lessons WHERE day=? AND lesson_time_id = ? AND teacher_id = ?";
	private static final String GET_COUNT_LESSONS_QUERY = "SELECT count(id) FROM lessons";

	private JdbcTemplate jdbcTemplate;
	private LessonRowMapper rowMapper;

	public LessonDao(JdbcTemplate jdbcTemplate, LessonRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void create(Lesson lesson) {
		LOG.debug("Created lesson {}", lesson);
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_LESSON_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, lesson.getCourse().getId());
			ps.setInt(2, lesson.getTeacher().getId());
			ps.setDate(3, java.sql.Date.valueOf(lesson.getDay()));
			ps.setInt(4, lesson.getLessonTime().getId());
			ps.setInt(5, lesson.getAuditorium().getId());
			return ps;
		}, holder);

		lesson.setId((int) holder.getKeys().get("id"));

		lesson.getGroups().stream()
				.forEach(g -> jdbcTemplate.update(CREATE_LESSONS_GROUPS_QUERY, lesson.getId(), g.getId()));
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void deleteById(int id) {
		LOG.debug("Deleted lesson, id = {}", id);
		jdbcTemplate.update(DELETE_LESSONS_GROUPS_QUERY, id);
		jdbcTemplate.update(DELETE_LESSON_QUERY, id);
	}

	public Lesson getById(int id) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("Lesson not found by id = {}", id);
			return null;
		}
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void update(Lesson lesson) {
		LOG.debug("Updated to lesson {}", lesson);
		jdbcTemplate.update(UPDATE_LESSON_QUERY, lesson.getCourse().getId(), lesson.getTeacher().getId(),
				lesson.getDay(), lesson.getLessonTime().getOrderNumber(), lesson.getAuditorium().getId(), lesson.getId());
		jdbcTemplate.update(DELETE_LESSONS_GROUPS_QUERY, lesson.getId());
		lesson.getGroups().stream()
				.forEach(g -> jdbcTemplate.update(CREATE_LESSONS_GROUPS_QUERY, lesson.getId(), g.getId()));
	}

	public Page<Lesson> getAll(Pageable pageable) {
		int total = jdbcTemplate.queryForObject(GET_COUNT_LESSONS_QUERY, Integer.class);
		List<Lesson> lessons = jdbcTemplate.query(GET_ALL_PAGEABLE_QUERY, rowMapper, pageable.getPageSize(),
				pageable.getOffset());
		return new PageImpl<>(lessons, pageable, total);
	}

	public List<Lesson> getByDayTime(LocalDate day, LessonTime lessonTime) {
		return jdbcTemplate.query(GET_BY_DAY_TIME_QUERY, rowMapper, day, lessonTime.getId());
	}

	public Lesson getByDayTimeAuditorium(LocalDate day, LessonTime lessonTime, Auditorium auditorium) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_DAY_TIME_AUDITORIUM_QUERY, rowMapper, day, lessonTime.getId(),
					auditorium.getId());
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("Lesson not found by: day = {}, lessonTime = {},auditorium = {}", day, lessonTime, auditorium);
			return null;
		}
	}

	public Lesson getByDayTimeTeacher(LocalDate day, LessonTime lessonTime, Teacher teacher) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_DAY_TIME_TEACHER_QUERY, rowMapper, day, lessonTime.getId(),
					teacher.getId());
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("Lesson not found by: day = {}, lessonTime = {},teacher = {}", day, lessonTime, teacher);
			return null;
		}
	}
}
