package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
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

import ru.tsar.university.dao.mapper.LessonTimeRowMapper;
import ru.tsar.university.model.LessonTime;

@Component
public class LessonTimeDao {

	private static final Logger LOG = LoggerFactory.getLogger(LessonTimeDao.class);

	private static final String ADD_LESSON_TIME_QUERY = "INSERT INTO lessons_time(order_number,start_time,end_time) VALUES(?,?,?)";
	private static final String DELETE_LESSON_TIME_QUERY = "DELETE FROM lessons_time where id =?";
	private static final String GET_BY_ORDER_NUMBER_QUERY = "SELECT * FROM lessons_time WHERE order_number=?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM lessons_time WHERE id=?";
	private static final String UPDATE_LESSON_TIME_QUERY = "UPDATE lessons_time SET start_time=?,end_time=?, WHERE id=?";
	private static final String GET_ALL_QUERY = "SELECT * FROM lessons_time LIMIT ? OFFSET ?";
	private static final String GET_COUNT_LESSON_TIME_QUERY = "SELECT count(id) FROM lessons_time";
	
	private JdbcTemplate jdbcTemplate;
	private LessonTimeRowMapper rowMapper;

	public LessonTimeDao(JdbcTemplate jdbcTemplate, LessonTimeRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public void create(LessonTime lessonTime) {
		LOG.debug("Created LessonTime {}", lessonTime);
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(ADD_LESSON_TIME_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, lessonTime.getOrderNumber());
			ps.setTime(2, Time.valueOf(lessonTime.getStartTime()));
			ps.setTime(3, Time.valueOf(lessonTime.getEndTime()));

			return ps;
		}, holder);
		lessonTime.setId((int) holder.getKeys().get("id"));
	}

	public void deleteById(int id) {
		LOG.debug("Deleted lessonTime, id = {}", id);
		jdbcTemplate.update(DELETE_LESSON_TIME_QUERY, id);
	}

	public LessonTime getById(int id) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("LessonTime not fount by id = {}", id);
			return null;
		}
	}

	public LessonTime getByOrder(int order) {
		return jdbcTemplate.queryForObject(GET_BY_ORDER_NUMBER_QUERY, rowMapper, order);
	}

	public void update(LessonTime lessonTime) {
		LOG.debug("Updated LessonTime {}", lessonTime);
		jdbcTemplate.update(UPDATE_LESSON_TIME_QUERY, lessonTime.getStartTime(), lessonTime.getEndTime(),
				lessonTime.getId());
	}

	public Page<LessonTime> getAll(Pageable pageable) {
		int total = jdbcTemplate.queryForObject(GET_COUNT_LESSON_TIME_QUERY, Integer.class);
		List<LessonTime> lessonTimes = jdbcTemplate.query(GET_ALL_QUERY, rowMapper, pageable.getPageSize() ,pageable.getOffset());
		return new PageImpl<>(lessonTimes, pageable, total);
	}
}
