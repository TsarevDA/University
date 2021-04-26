package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static ru.tsar.university.service.LessonTimeServiceTest.TestData.*;

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
		LocalTime startTime = LocalTime.of(8,0);
		LocalTime endTime = LocalTime.of(9,0);
		LessonTime expected = LessonTime.builder().orderNumber(1).startTime(startTime).endTime(endTime).build();

		lessonTimeService.create(expected);

		verify(lessonTimeDao).create(expected);
	}

	@Test
	void givenWrongLessonTime_whenCreate_thenNoAction() {
		LocalTime startTime = LocalTime.of(17,0);
		LocalTime endTime = LocalTime.of(9,0);
		LessonTime expected = LessonTime.builder().orderNumber(1).startTime(startTime).endTime(endTime).build();

		lessonTimeService.create(expected);

		verify(lessonTimeDao, never()).create(expected);
	}

	@Test
	void givenLessonsTime_whenGetAll_thenCallDaoMEthod() {
		LessonTime lessonTime1 = lessonTime_1;
		LessonTime lessonTime2 = lessonTime_2;
		List<LessonTime> expected = new ArrayList<>();
		expected.add(lessonTime1);
		expected.add(lessonTime2);

		when(lessonTimeDao.getAll()).thenReturn(expected);

		List<LessonTime> actual = lessonTimeService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenNewLessonTime_whenUpdate_thenCallDaoMethod() {
		LessonTime newLessonTime = lessonTime_1;
		LessonTime oldLessonTime = lessonTime_3;
		when(lessonTimeDao.getById(1)).thenReturn(oldLessonTime);
		lessonTimeService.update(newLessonTime);

		verify(lessonTimeDao).update(newLessonTime);
	}

	@Test
	void givenWrongLessonTime_whenUpdate_thenNoAction() {
		LessonTime oldLessonTime = lessonTime_3;
		LessonTime newLessonTime = lessonTime_4;

		when(lessonTimeDao.getById(1)).thenReturn(oldLessonTime);
		lessonTimeService.update(newLessonTime);

		verify(lessonTimeDao, never()).update(newLessonTime);
	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		LessonTime lessonTime = lessonTime_1;

		when(lessonTimeDao.getById(1)).thenReturn(lessonTime);
		lessonTimeService.deleteById(1);

		verify(lessonTimeDao).deleteById(1);
	}

	@Test
	void givenId_whenDeleteById_thenNoAction() {
		
		lessonTimeService.deleteById(1);

		verify(lessonTimeDao, never()).deleteById(1);
	}
	
	interface TestData {
		LocalTime startTime_1 = LocalTime.of(8,0);
		LocalTime endTime_1 = LocalTime.of(9,0);
		LocalTime startTime_2 = LocalTime.of(9,0);
		LocalTime endTime_2 = LocalTime.of(10,0);
		LessonTime lessonTime_1 = LessonTime.builder().id(1).orderNumber(1).startTime(startTime_1).endTime(endTime_1).build();
		LessonTime lessonTime_2 = LessonTime.builder().id(2).orderNumber(2).startTime(startTime_2).endTime(endTime_2).build();
		LessonTime lessonTime_3 = LessonTime.builder().id(1).orderNumber(1).startTime(startTime_2).endTime(endTime_2).build();
		LessonTime lessonTime_4 = LessonTime.builder().id(1).orderNumber(1).startTime(endTime_2).endTime(startTime_1).build();
	}
}
