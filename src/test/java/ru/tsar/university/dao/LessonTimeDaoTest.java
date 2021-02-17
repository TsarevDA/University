package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.SpringConfig;
import ru.tsar.university.SpringTestConfig;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.LessonTime;

class LessonTimeDaoTest {

	final static private String GET_COURSES_REQUEST = "SELECT lt.* FROM lessons_time lt";
	final static private String GET_COUNT_BY_ID_REQUEST = "select count(*) FROM lessons_time WHERE id=?";
	final static private String CREATE_LESSON_TIME_QUERY = "INSERT INTO lessons_time(order_number,start_time,end_time) VALUES(?,?,?)";

	private AnnotationConfigApplicationContext context;
	private JdbcTemplate jdbcTemplate;
	private LessonTimeDao lessonTimeDao;

	@BeforeEach
	void setUp() {
		context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		context.getBean("databasePopulator", DatabasePopulator.class);
		this.jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		this.lessonTimeDao = context.getBean("lessonTimeDao", LessonTimeDao.class);
	}

	@AfterEach
	void setDown() {
		context.close();
	}

	@Test
	void setLessonTime_whenCreate_thenCreateLessonTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("08:00", formatter);
		LocalTime endTime = LocalTime.parse("09:00", formatter);
		LessonTime expected = new LessonTime(1, startTime, endTime);
		lessonTimeDao.create(expected);
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons_time", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteOrder() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("10:00", formatter);
		LocalTime endTime = LocalTime.parse("11:00", formatter);
		LessonTime lessonTime = new LessonTime(3, startTime, endTime);
		lessonTimeDao.create(lessonTime);
		lessonTimeDao.deleteById(lessonTime.getId());

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons_time", "id = " + lessonTime.getId());
		assertEquals(0, actual);
	}

	@Test
	void setOrder_whenGetByOrder_thenGetOrder() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", formatter);
		LocalTime endTime = LocalTime.parse("10:00", formatter);
		LessonTime expected = new LessonTime(2, startTime, endTime);
		lessonTimeDao.create(expected);
		LessonTime actual = lessonTimeDao.getByOrder(expected.getOrderNumber());
		assertEquals(expected, actual);
	}

	@Test
	void setLessonsTime_whenGetAll_thenLessonsTimeList() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", formatter);
		LocalTime endTime = LocalTime.parse("10:00", formatter);
		LessonTime first = new LessonTime(1, startTime, endTime);
		lessonTimeDao.create(first);

		startTime = LocalTime.parse("10:00", formatter);
		endTime = LocalTime.parse("11:00", formatter);
		LessonTime second = new LessonTime(2, startTime, endTime);
		lessonTimeDao.create(second);

		List<LessonTime> expected = new ArrayList<>();
		expected.add(first);
		expected.add(second);

		List<LessonTime> actual = lessonTimeDao.getAll();
		assertEquals(expected, actual);
	}
}
