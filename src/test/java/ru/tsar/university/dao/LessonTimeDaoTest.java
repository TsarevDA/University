package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.config.SpringTestConfig;
import ru.tsar.university.model.LessonTime;

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql("/lessonTimeData.sql")

class LessonTimeDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LessonTimeDao lessonTimeDao;

	@Test
	@DirtiesContext
	void givenNewLessonTime_whenCreate_thenCreated() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("08:00", formatter);
		LocalTime endTime = LocalTime.parse("09:00", formatter);
		LessonTime expected = LessonTime.builder().orderNumber(1).startTime(startTime).endTime(endTime).build();

		lessonTimeDao.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons_time", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	@DirtiesContext
	void givenId_whenDeleteById_thenDeleted() {

		lessonTimeDao.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons_time", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	@DirtiesContext
	void givenOrder_whenGetByOrder_thenLessonTimeFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", formatter);
		LocalTime endTime = LocalTime.parse("10:00", formatter);
		LessonTime expected = LessonTime.builder().id(1).orderNumber(2).startTime(startTime).endTime(endTime).build();

		LessonTime actual = lessonTimeDao.getByOrder(2);

		assertEquals(expected, actual);
	}

	@Test
	@DirtiesContext
	void givenLessonsTime_whenGetAll_thenLessonsTimeListFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", formatter);
		LocalTime endTime = LocalTime.parse("10:00", formatter);
		LessonTime lessonTime1 = LessonTime.builder().id(1).orderNumber(2).startTime(startTime).endTime(endTime)
				.build();
		startTime = LocalTime.parse("10:00", formatter);
		endTime = LocalTime.parse("11:00", formatter);
		LessonTime lessonTime2 = LessonTime.builder().id(2).orderNumber(3).startTime(startTime).endTime(endTime)
				.build();
		List<LessonTime> expected = new ArrayList<>();
		expected.add(lessonTime1);
		expected.add(lessonTime2);

		List<LessonTime> actual = lessonTimeDao.getAll();

		assertEquals(expected, actual);
	}
}
