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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ru.tsar.university.dao.LessonTimeDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.exceptions.InvalidTimeIntervalException;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.LessonTime;

@ExtendWith(MockitoExtension.class)
class LessonTimeServiceTest {

	@InjectMocks
	private LessonTimeService lessonTimeService;
	@Mock
	private LessonTimeDao lessonTimeDao;

	@Test
	void givenNewLessonTime_whenCreate_thenCallDaoMethod() {
		LocalTime startTime = LocalTime.of(8, 0);
		LocalTime endTime = LocalTime.of(9, 0);
		LessonTime expected = LessonTime.builder().orderNumber(1).startTime(startTime).endTime(endTime).build();

		lessonTimeService.create(expected);

		verify(lessonTimeDao).create(expected);
	}

	@Test
	void givenWrongLessonTime_whenCreate_thenTimeNotCorrectException() {
		LocalTime startTime = LocalTime.of(17, 0);
		LocalTime endTime = LocalTime.of(9, 0);
		LessonTime expected = LessonTime.builder().orderNumber(1).startTime(startTime).endTime(endTime).build();

		Exception exception = assertThrows(InvalidTimeIntervalException.class, () -> lessonTimeService.create(expected));

		String expectedMessage = "This time " + expected + " has not correct format";
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void givenLessonsTime_whenGetAll_thenCallDaoMEthod() {
		LessonTime lessonTime1 = lessonTime_1;
		LessonTime lessonTime2 = lessonTime_2;
		List<LessonTime> lessonTimes = new ArrayList<>();
		lessonTimes.add(lessonTime1);
		lessonTimes.add(lessonTime2);

		Pageable pageabele = PageRequest.of(0, 5);
		Page<LessonTime> expected = new PageImpl<>(lessonTimes, pageabele, lessonTimes.size());
		
		when(lessonTimeDao.getAll(pageabele)).thenReturn(expected);

		Page<LessonTime> actual = lessonTimeService.getAll(pageabele);

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
	void givenWrongLessonTime_whenUpdate_thenTimeNotCorrectException() {
		LessonTime oldLessonTime = lessonTime_3;
		LessonTime newLessonTime = lessonTime_4;

		when(lessonTimeDao.getById(1)).thenReturn(oldLessonTime);

		Exception exception = assertThrows(InvalidTimeIntervalException.class, () -> lessonTimeService.update(newLessonTime));

		String expectedMessage = "This time " + newLessonTime + " has not correct format";
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		LessonTime lessonTime = lessonTime_1;

		when(lessonTimeDao.getById(1)).thenReturn(lessonTime);

		lessonTimeService.deleteById(1);

		verify(lessonTimeDao).deleteById(1);
	}

	@Test
	void givenId_whenDeleteById_thenLessonTimeNotExistException() {

		Exception exception = assertThrows(EntityNotFoundException.class, () -> lessonTimeService.deleteById(1));

		String expectedMessage = "LessonTime with id = 1 does not exist";
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);
	}

	interface TestData {
		LocalTime startTime_1 = LocalTime.of(8, 0);
		LocalTime endTime_1 = LocalTime.of(9, 0);
		LocalTime startTime_2 = LocalTime.of(9, 0);
		LocalTime endTime_2 = LocalTime.of(10, 0);
		LessonTime lessonTime_1 = LessonTime.builder()
				.id(1)
				.orderNumber(1)
				.startTime(startTime_1)
				.endTime(endTime_1)
				.build();
		LessonTime lessonTime_2 = LessonTime.builder()
				.id(2).orderNumber(2)
				.startTime(startTime_2)
				.endTime(endTime_2)
				.build();
		LessonTime lessonTime_3 = LessonTime.builder()
				.id(1)
				.orderNumber(1)
				.startTime(startTime_2)
				.endTime(endTime_2)
				.build();
		LessonTime lessonTime_4 = LessonTime.builder()
				.id(1)
				.orderNumber(1)
				.startTime(endTime_2)
				.endTime(startTime_1)
				.build();
	}
}
