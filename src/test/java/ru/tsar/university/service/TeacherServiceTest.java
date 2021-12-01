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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

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
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher1);
		teachers.add(teacher2);

		Pageable pageabele = PageRequest.of(0, 5);
		Page<Teacher> expected = new PageImpl<>(teachers, pageabele, teachers.size());
		
		when(teacherDao.getAll(pageabele)).thenReturn(expected);
		Page<Teacher> actual = teacherService.getAll(pageabele);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacher_whenUpdate_thenCallDaoMethod() {
		Teacher expected = teacher_1;
		Teacher oldValue = teacher_3;

		when(teacherDao.getById(1)).thenReturn(oldValue);

		teacherService.update(expected);

		verify(teacherDao).update(expected);

	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		Teacher teacher = teacher_1;

		when(teacherDao.getById(1)).thenReturn(teacher);

		teacherService.deleteById(1);

		verify(teacherDao).deleteById(1);
	}

	@Test
	void givenExistId_whenDeleteById_thenNoAction() {

		Exception exception = assertThrows(EntityNotFoundException.class, () -> teacherService.deleteById(1));

		String expectedMessage = "Teacher with id = 1 does not exist";
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);
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
