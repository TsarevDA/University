package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.tsar.university.dao.LessonTimeDao;
import ru.tsar.university.model.LessonTime;

@ExtendWith(MockitoExtension.class)
class LessonTimeServiceTest {

	@InjectMocks
	private LessonTimeService lessonTimeService;
	@Mock
	private LessonTimeDao lessonTimeDao;

	@Test
	void givenNewLessonTime_whenCreate_thenCallDaoMethod() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("08:00", formatter);
		LocalTime endTime = LocalTime.parse("09:00", formatter);
		LessonTime expected = LessonTime.builder().orderNumber(1).startTime(startTime).endTime(endTime).build();

		lessonTimeService.create(expected);

		verify(lessonTimeDao).create(expected);
	}

	@Test
	void givenWrongLessonTime_whenCreate_thenNoAction() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("17:00", formatter);
		LocalTime endTime = LocalTime.parse("09:00", formatter);
		LessonTime expected = LessonTime.builder().orderNumber(1).startTime(startTime).endTime(endTime).build();

		lessonTimeService.create(expected);

		verify(lessonTimeDao, never()).create(expected);
	}

	@Test
	void givenLessonsTime_whenGetAll_thenCallDaoMEthod() {
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

		when(lessonTimeDao.getAll()).thenReturn(expected);

		List<LessonTime> actual = lessonTimeService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenNewLessonTime_whenUpdate_thenCallDaoMethod() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("08:00", formatter);
		LocalTime endTime = LocalTime.parse("09:00", formatter);
		LessonTime newLessonTime = LessonTime.builder().id(1).orderNumber(1).startTime(startTime).endTime(endTime)
				.build();
		LessonTime oldLessonTime = LessonTime.builder().id(1).orderNumber(3).startTime(startTime).endTime(endTime)
				.build();

		when(lessonTimeDao.getById(1)).thenReturn(oldLessonTime);
		lessonTimeService.update(newLessonTime);

		verify(lessonTimeDao).update(newLessonTime);
	}

	@Test
	void givenWrongLessonTime_whenUpdate_thenNoAction() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime oldStartTime = LocalTime.parse("09:00", formatter);
		LocalTime oldEndTime = LocalTime.parse("09:00", formatter);
		LocalTime newStartTime = LocalTime.parse("17:00", formatter);
		LocalTime newEndTime = LocalTime.parse("09:00", formatter);
		LessonTime oldLessonTime = LessonTime.builder().id(1).orderNumber(1).startTime(oldStartTime).endTime(oldEndTime)
				.build();
		LessonTime newLessonTime = LessonTime.builder().id(1).orderNumber(1).startTime(newStartTime).endTime(newEndTime)
				.build();

		when(lessonTimeDao.getById(1)).thenReturn(oldLessonTime);
		lessonTimeService.update(newLessonTime);

		verify(lessonTimeDao, never()).update(newLessonTime);
	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", formatter);
		LocalTime endTime = LocalTime.parse("10:00", formatter);
		LessonTime lessonTime = LessonTime.builder().id(1).orderNumber(2).startTime(startTime).endTime(endTime).build();

		when(lessonTimeDao.getById(1)).thenReturn(lessonTime);
		lessonTimeService.deleteById(1);

		verify(lessonTimeDao).deleteById(1);
	}

	@Test
	void givenId_whenDeleteById_thenNoAction() {

		lessonTimeService.deleteById(1);

		verify(lessonTimeDao, never()).deleteById(1);
	}
}
