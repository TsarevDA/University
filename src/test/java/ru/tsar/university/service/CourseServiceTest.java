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

import ru.tsar.university.dao.CourseDao;
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
	void givenExistCourse_whenCreate_thenNoAction() {
		Course course = Course.builder()
				.name("Math")
				.description("Science about numbers")
				.build();
	
		when(courseDao.getByName(course)).thenReturn(course_1);

		courseService.create(course);

		verify(courseDao, never()).create(course);
	}

	@Test
	void givenId_whenGetById_thenCourseFound() {
		when(courseDao.getById(1)).thenReturn(course_1);

		Course actual = courseService.getById(1);

		assertEquals(course_1, actual);
	}

	@Test
	void givenCourses_whenGetAll_thenCoursesListFound() {
		List<Course> expected = new ArrayList<>();
		expected.add(course_1);
		expected.add(course_2);

		when(courseDao.getAll()).thenReturn(expected);

		List<Course> actual = courseService.getAll();

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
	void givenNameDublicateCourse_whenUpdate_thenNoAction() {
		when(courseDao.getById(1)).thenReturn(course_1);
		when(courseDao.getByName(course_2)).thenReturn(dublicatedCourse_1);

		courseService.update(course_2);
		verify(courseDao, never()).update(course_2);
	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		Course course = Course.builder().id(1).name("Math").description("Science about numbers").build();

		when(courseDao.getById(1)).thenReturn(course);
		courseService.deleteById(1);

		verify(courseDao).deleteById(1);
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
