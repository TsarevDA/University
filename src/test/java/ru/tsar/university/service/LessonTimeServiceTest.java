package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.config.SpringTestConfig;
import ru.tsar.university.model.LessonTime;

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql("/lessonTimeData.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)

class LessonTimeServiceTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LessonTimeService lessonTimeService;

	@Test
	void givenNewLessonTime_whenCreate_thenCreated() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("08:00", formatter);
		LocalTime endTime = LocalTime.parse("09:00", formatter);
		LessonTime expected = LessonTime.builder().orderNumber(1).startTime(startTime).endTime(endTime).build();

		lessonTimeService.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons_time", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {

		lessonTimeService.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons_time", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	void givenOrder_whenGetByOrder_thenLessonTimeFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", formatter);
		LocalTime endTime = LocalTime.parse("10:00", formatter);
		LessonTime expected = LessonTime.builder().id(1).orderNumber(2).startTime(startTime).endTime(endTime).build();

		LessonTime actual = lessonTimeService.getByOrder(2);

		assertEquals(expected, actual);
	}

	@Test
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

		List<LessonTime> actual = lessonTimeService.getAll();

		assertEquals(expected, actual);
	}
}
