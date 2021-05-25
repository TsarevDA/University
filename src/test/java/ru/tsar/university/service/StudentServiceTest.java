package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static ru.tsar.university.service.StudentServiceTest.TestData.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.exceptions.StudentExistException;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(StudentServiceTest.class);
	@InjectMocks
	private StudentService studentService;
	@Mock
	private StudentDao studentDao;

	@Test
	void givenStudent_whenCreate_thenCallDaoMethod() {
		Student expected = Student.builder()
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.build();

		studentService.create(expected);

		verify(studentDao).create(expected);
	}

	@Test
	void givenId_whenGetById_thenStudentFound() {
		Student expected = student_1;

		when(studentDao.getById(1)).thenReturn(expected);

		Student actual = studentService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudents_whenGetAll_thenStudentsListFound() {
		Student student1 = student_1;
		Student student2 = student_2;
		List<Student> expected = new ArrayList<>();
		expected.add(student1);
		expected.add(student2);

		when(studentDao.getAll()).thenReturn(expected);
		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenStudent_whenUpdate_thenCallDaoMethod() {
		Student expected = student_1;
		Student oldValue = student_3;

		when(studentDao.getById(1)).thenReturn(oldValue);

		try {
			studentService.update(expected);
		} catch (StudentExistException e) {
			LOG.debug(e.getMessage());
		}
		verify(studentDao).update(expected);

	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		Student student = student_1;

		when(studentDao.getById(1)).thenReturn(student);

		try {
			studentService.deleteById(1);
		} catch (StudentExistException e) {
			LOG.debug(e.getMessage());
		}

		verify(studentDao).deleteById(1);
	}

	@Test
	void givenExistId_whenDeleteById_thenNoAction() {

		try {
			studentService.deleteById(1);
		} catch (StudentExistException e) {
			LOG.debug(e.getMessage());
		}

		verify(studentDao, never()).deleteById(1);
	}

	interface TestData {
		Student student_1 = Student.builder()
				.id(1)
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.build();
		Student student_2 = Student.builder()
				.id(2).firstName("Petr")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1992, Month.MAY, 3))
				.email("mail11111@mail.ru").phone("880899908080")
				.address("Petrov street, 25-5")
				.build();
		Student student_3 = Student.builder()
				.id(1)
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1991, Month.JANUARY, 1))
				.email("100@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.build();
	}
}
