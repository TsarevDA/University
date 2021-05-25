package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static ru.tsar.university.service.TeacherServiceTest.TestData.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.exceptions.TeacherExistException;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(TeacherServiceTest.class);
	@InjectMocks
	private TeacherService teacherService;
	@Mock
	private TeacherDao teacherDao;

	@Test
	void givenTeacher_whenCreate_thenCreated() {
		Teacher teacher = Teacher.builder()
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.courses(new ArrayList<Course>())
				.build();

		teacherService.create(teacher);

		verify(teacherDao).create(teacher);
	}

	@Test
	void givenId_whenGetById_thenTeacherFound() {
		Teacher expected = teacher_1;

		when(teacherDao.getById(1)).thenReturn(expected);

		Teacher actual = teacherService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeachers_whenGetAll_thenTeachersListFound() {
		Teacher teacher1 = teacher_1;
		Teacher teacher2 = teacher_2;
		List<Teacher> expected = new ArrayList<>();
		expected.add(teacher1);
		expected.add(teacher2);

		when(teacherDao.getAll()).thenReturn(expected);
		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacher_whenUpdate_thenCallDaoMethod() {
		Teacher expected = teacher_1;
		Teacher oldValue = teacher_3;

		when(teacherDao.getById(1)).thenReturn(oldValue);

		try {
			teacherService.update(expected);
		} catch (TeacherExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		verify(teacherDao).update(expected);

	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		Teacher teacher = teacher_1;

		when(teacherDao.getById(1)).thenReturn(teacher);

		try {
			teacherService.deleteById(1);
		} catch (TeacherExistException e) {
			LOG.debug(e.getMessage());
		}

		verify(teacherDao).deleteById(1);
	}

	@Test
	void givenExistId_whenDeleteById_thenNoAction() {

		try {
			teacherService.deleteById(1);
		} catch (TeacherExistException e) {
			LOG.debug(e.getMessage());
		}

		verify(teacherDao, never()).deleteById(1);
	}

	interface TestData {
		Course course_1 = Course.builder()
				.name("Biology")
				.description("Science about plants")
				.build();
		Course course_2 = Course.builder()
				.id(1).name("Astronomy")
				.description("Science about stars and deep space")
				.build();
		List<Course> courses_1 = Arrays.asList(course_1);
		List<Course> courses_2 = Arrays.asList(course_2);
		Teacher teacher_1 = Teacher.builder()
				.id(1)
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.courses(courses_1)
				.build();
		Teacher teacher_2 = Teacher.builder()
				.id(2)
				.firstName("Petr")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1992, Month.MAY, 3))
				.email("mail11111@mail.ru")
				.phone("880899908080")
				.address("Petrov street, 25-5")
				.courses(courses_2)
				.build();
		Teacher teacher_3 = Teacher.builder()
				.id(1)
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1991, Month.JANUARY, 1))
				.email("100@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.courses(courses_1)
				.build();
	}
}
