package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
		Course course = Course.builder().name("Biology").description("Science about plants").build();
	
		courseService.create(course);
		
		Mockito.verify(courseDao).create(course);
	}
	
	@Test
	void givenExistCourse_whenCreate_thenCallDaoMethod() {
		Course course = Course.builder().name("Biology").description("Science about plants").build();
		
		Mockito.when(courseDao.getByName(course)).thenReturn(course);
		
		courseService.create(course);
		
		Mockito.verify(courseDao, Mockito.never()).create(course);
	}

	@Test
	void givenId_whenGetById_thenCourseFound() {
		Course expected = Course.builder().id(1).name("Astronomy").description("Science about stars and deep space")
				.build();
		
		Mockito.when(courseDao.getById(1)).thenReturn(expected);

		Course actual = courseService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenCourses_whenGetAll_thenCoursesListFound() {
		Course course1 = Course.builder().id(1).name("Astronomy").description("Science about stars and deep space")
				.build();
		Course course2 = Course.builder().id(2).name("Math").description("Science about numbers").build();
		List<Course> expected = new ArrayList<>();
		expected.add(course1);
		expected.add(course2);
		
		Mockito.when(courseDao.getAll()).thenReturn(expected);
		
		List<Course> actual = courseService.getAll();

		assertEquals(expected, actual);
	}
	
	@Test
	void givenCourse_whenUpdate_thenCallDaoMethod() {
		Course newCourse = Course.builder().id(1).name("Math").description("Science about numbers").build();
		Course oldCourse = Course.builder().id(1).name("Math").description("Science").build();

		Mockito.when(courseDao.getById(1)).thenReturn(oldCourse);
		Mockito.when(courseDao.getByName(newCourse)).thenReturn(oldCourse);

		courseService.update(newCourse);
		
		Mockito.verify(courseDao).update(newCourse);
	}

	@Test
	void givenNameDublicateCourse_whenUpdate_thenNoAction() {
		Course newCourse = Course.builder().id(1).name("Math").description("Science about numbers").build();
		Course oldCourse = Course.builder().id(1).name("Biology").description("Science about numbers").build();
		Course dublicateCourse = Course.builder().id(2).name("Math").description("Science about numbers").build();

		Mockito.when(courseDao.getById(1)).thenReturn(oldCourse);
		Mockito.when(courseDao.getByName(newCourse)).thenReturn(dublicateCourse);

		courseService.update(newCourse);
		Mockito.verify(courseDao, Mockito.never()).update(newCourse);
	}
	
	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		Course course = Course.builder().id(1).name("Math").description("Science about numbers").build();
		
		Mockito.when(courseDao.getById(1)).thenReturn(course);
		courseService.deleteById(1);
		
		Mockito.verify(courseDao).deleteById(1);
	}

	
}
