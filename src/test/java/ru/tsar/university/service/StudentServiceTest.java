package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.tsar.university.dao.StudentDao;
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = Student.builder().firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		
		studentService.create(expected);

		Mockito.verify(studentDao).create(expected);
	}

	

	@Test
	void givenId_whenGetById_thenStudentFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();

		Mockito.when(studentDao.getById(1)).thenReturn(expected);
		
		Student actual = studentService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenStudents_whenGetAll_thenStudentsListFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(2).firstName("Petr").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1992-05-03", formatter)).email("mail11111@mail.ru").phone("880899908080")
				.address("Petrov street, 25-5").build();
		List<Student> expected = new ArrayList<>();
		expected.add(student1);
		expected.add(student2);

		Mockito.when(studentDao.getAll()).thenReturn(expected);
		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudent_whenUpdate_thenCallDaoMethod() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student oldValue = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1991-01-01", formatter)).email("100@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();;

		Mockito.when(studentDao.getById(1)).thenReturn(oldValue);
		

		studentService.update(expected);
		Mockito.verify(studentDao).update(expected);
	

	}
	
	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student student = Student.builder().firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		
		Mockito.when(studentDao.getById(1)).thenReturn(student);
		
		studentService.deleteById(1);
		
		Mockito.verify(studentDao).deleteById(1);
	}
	
	@Test
	void givenExistId_whenDeleteById_thenNoAction() {
	
		studentService.deleteById(1);
		
		Mockito.verify(studentDao, Mockito.never()).deleteById(1);
	}
}
