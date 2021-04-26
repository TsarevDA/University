package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tsar.university.dao.LessonTimeDaoTest.TestData.*;

import java.time.LocalTime;
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

class LessonTimeDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LessonTimeDao lessonTimeDao;

	@Test
	void givenNewLessonTime_whenCreate_thenCreated() {
		LessonTime expected = LessonTime.builder()
				.orderNumber(1)
				.startTime(startTime_1)
				.endTime(endTime_2)
				.build();

		lessonTimeDao.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons_time", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {

		lessonTimeDao.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons_time", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	void givenOrder_whenGetByOrder_thenLessonTimeFound() {
		LessonTime expected = lessonTime_1;

		LessonTime actual = lessonTimeDao.getByOrder(2);

		assertEquals(expected, actual);
	}

	@Test
	void givenLessonsTime_whenGetAll_thenLessonsTimeListFound() {
		List<LessonTime> expected = new ArrayList<>();
		expected.add(lessonTime_1);
		expected.add(lessonTime_2);

		List<LessonTime> actual = lessonTimeDao.getAll();

		assertEquals(expected, actual);
	}

	interface TestData {
		LocalTime startTime_1 = LocalTime.of(9, 0);
		LocalTime endTime_1 = LocalTime.of(10, 0);
		LessonTime lessonTime_1 = LessonTime.builder()
				.id(1)
				.orderNumber(2)
				.startTime(startTime_1)
				.endTime(endTime_1)
				.build();
		LocalTime startTime_2 = LocalTime.of(10, 0);
		LocalTime endTime_2 = LocalTime.of(11, 0);
		LessonTime lessonTime_2 = LessonTime
				.builder().id(2)
				.orderNumber(3)
				.startTime(startTime_2)
				.endTime(endTime_2)
				.build();
	}
}
