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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

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
		List<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);

		Pageable pageabele = PageRequest.of(0, 5);
		Page<Student> expected = new PageImpl<>(students, pageabele, students.size());
		
		when(studentDao.getAll(pageabele)).thenReturn(expected);
		Page<Student> actual = studentService.getAll(pageabele);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudent_whenUpdate_thenCallDaoMethod() {
		Student expected = student_1;
		Student oldValue = student_3;

		when(studentDao.getById(1)).thenReturn(oldValue);

		studentService.update(expected);

		verify(studentDao).update(expected);

	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		Student student = student_1;

		when(studentDao.getById(1)).thenReturn(student);

		studentService.deleteById(1);

		verify(studentDao).deleteById(1);
	}

	@Test
	void givenExistId_whenDeleteById_thenStudentNotExistException() {

		Exception exception = assertThrows(EntityNotFoundException.class, () -> studentService.deleteById(1));

		String expectedMessage = "Student with id = 1 does not exist";
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);

	}

	interface TestData {
		Student student_1 = Student.builder()
				.id(1)
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5")
				.build();
		Student student_2 = Student.builder()
				.id(2)
				.firstName("Petr")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1992, Month.MAY, 3))
				.email("mail11111@mail.ru")
				.phone("880899908080")
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
