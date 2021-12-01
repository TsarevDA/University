package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static ru.tsar.university.service.CourseServiceTest.TestData.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.exceptions.NotUniqueNameException;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

	@InjectMocks
	private CourseService courseService;
	@Mock
	private CourseDao courseDao;

	@Test
	void givenNewCourse_whenCreate_thenCallDaoMethod() {
		Course course = Course.builder()
				.name("Biology")
				.description("Science about plants")
				.build();

		courseService.create(course);

		verify(courseDao).create(course);
	}

	@Test
	void givenExistCourse_whenCreate_thenThrowNotUniqueNameException() {
		Course course = Course.builder()
				.name("Math")
				.description("Science about numbers")
				.build();

		when(courseDao.getByName(course)).thenReturn(course_1);

		Exception exception = assertThrows(NotUniqueNameException.class, () -> courseService.create(course));

		String expectedMessage = "This name is not unique: " + course.getName();
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void givenId_whenGetById_thenCourseFound() {
		when(courseDao.getById(1)).thenReturn(course_1);

		Course actual = courseService.getById(1);

		assertEquals(course_1, actual);
	}

	@Test
	void givenCourses_whenGetAll_thenCoursesListFound() {
		List<Course> courses = new ArrayList<>();
		courses.add(course_1);
		courses.add(course_2);
		Pageable pageable = PageRequest.of(0, 5);
		Page<Course> expected = new PageImpl<>(courses, pageable, courses.size());

		when(courseDao.getAll(pageable)).thenReturn(expected);
		Page<Course> actual = courseService.getAll(pageable);
		assertEquals(expected, actual);
	}

	@Test
	void givenCourse_whenUpdate_thenCallDaoMethod() {
		when(courseDao.getById(1)).thenReturn(course_1);
		when(courseDao.getByName(course_2)).thenReturn(course_1);

		courseService.update(course_2);

		verify(courseDao).update(course_2);
	}

	@Test
	void givenNameDublicateCourse_whenUpdate_thenNotUniqueNameException() {
		when(courseDao.getById(1)).thenReturn(course_1);
		when(courseDao.getByName(course_2)).thenReturn(dublicatedCourse_1);

		Exception exception = assertThrows(NotUniqueNameException.class, () -> courseService.update(course_2));

		String expectedMessage = "This name is not unique: " + course_2.getName();
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);

	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		Course course = Course.builder()
				.id(1)
				.name("Math")
				.description("Science about numbers")
				.build();

		when(courseDao.getById(1)).thenReturn(course);

		courseService.deleteById(1);

		verify(courseDao).deleteById(1);
	}

	@Test
	void givenNotExistedId_whenDeleteById_thenThrowCourseNotExistException() {
		when(courseDao.getById(1)).thenReturn(null);

		Exception exception = assertThrows(EntityNotFoundException.class, () -> courseService.deleteById(1));

		String expectedMessage = "Course with id = 1 does not exist";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	interface TestData {
		Course course_1 = Course.builder()
				.id(1)
				.name("Math")
				.description("Science about numbers")
				.build();
		Course course_2 = Course.builder()
				.id(1)
				.name("Biology")
				.description("Science about numbers")
				.build();
		Course dublicatedCourse_1 = Course.builder()
				.id(2)
				.name("Math")
				.description("Science about numbers")
				.build();
	}
}