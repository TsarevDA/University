package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import ru.tsar.university.TablesCreation;
import ru.tsar.university.model.LessonTime;

class LessonTimeDaoTest {

	final static private String GET_COURSES_REQUEST = "SELECT lt.* FROM lessons_time lt";
	final static private String GET_COUNT_BY_ID_REQUEST = "select count(*) FROM lessons_time WHERE id=?";
	final static private String CREATE_LESSON_TIME_QUERY = "INSERT INTO lessons_time(order_number,start_time,end_time) VALUES(?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LessonTimeDao lessonTimeDao;

	@BeforeEach
	void setUp() throws IOException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		lessonTimeDao = context.getBean("lessonTimeDao", LessonTimeDao.class);
		jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		TablesCreation tablesCreation = context.getBean("tablesCreation", TablesCreation.class);
		tablesCreation.createTables();
	}

	@Test
	void setLessonTime_whenCreate_thenCreateLessonTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("08:00", formatter);
		LocalTime endTime = LocalTime.parse("09:00", formatter);
		LessonTime expected = new LessonTime(1, startTime, endTime);
		lessonTimeDao.create(expected);
		List<LessonTime> lessonTimes = jdbcTemplate.query(GET_COURSES_REQUEST, (resultSet, rowNum) -> {
			LessonTime newLessonTime = new LessonTime(resultSet.getInt("id"), resultSet.getInt("order_number"),
					resultSet.getTime("start_time").toLocalTime(), resultSet.getTime("end_time").toLocalTime());
			return newLessonTime;
		});
		LessonTime actual = lessonTimes.get(lessonTimes.size() - 1);
		assertEquals(expected, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteOrder() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("10:00", formatter);
		LocalTime endTime = LocalTime.parse("11:00", formatter);
		LessonTime lessonTime = new LessonTime(3, startTime, endTime);
		lessonTimeDao.create(lessonTime);
		lessonTimeDao.deleteById(lessonTime.getId());

		int actual = jdbcTemplate.queryForObject(GET_COUNT_BY_ID_REQUEST, Integer.class, lessonTime.getId());
		assertEquals(0, actual);
	}

	@Test
	void setOrder_whenGetByOrder_thenGetOrder() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", formatter);
		LocalTime endTime = LocalTime.parse("10:00", formatter);
		LessonTime expected = new LessonTime(2, startTime, endTime);
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_LESSON_TIME_QUERY,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, expected.getOrderNumber());
			ps.setTime(2, java.sql.Time.valueOf(expected.getStartTime()));
			ps.setTime(3, java.sql.Time.valueOf(expected.getEndTime()));
			return ps;
		}, holder);
		expected.setId((int) holder.getKeys().get("id"));
		LessonTime actual = lessonTimeDao.getByOrder(expected.getOrderNumber());
		assertEquals(expected, actual);
	}
}
