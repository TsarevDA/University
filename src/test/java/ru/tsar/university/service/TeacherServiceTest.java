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

import ru.tsar.university.dao.TeacherDao;
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher teacher = Teacher.builder().firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").courses(new ArrayList<Course>()).build();

		teacherService.create(teacher);
		
		Mockito.verify(teacherDao).create(teacher);
	}

	@Test
	void givenId_whenGetById_thenTeacherFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher expected = Teacher.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").courses(new ArrayList<Course>()).build();

		Mockito.when(teacherDao.getById(1)).thenReturn(expected);
		
		Teacher actual = teacherService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenTeachers_whenGetAll_thenTeachersListFound() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<Course> courses = new ArrayList<>();
		Teacher teacher1 = Teacher.builder().firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		teacher1.setCourses(courses);
		Teacher teacher2 = Teacher.builder().firstName("Petr").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1992-05-03", formatter)).email("mail11111@mail.ru").phone("880899908080")
				.address("Petrov street, 25-5").build();
		teacher2.setCourses(courses);
		List<Teacher> expected = new ArrayList<>();
		expected.add(teacher1);
		expected.add(teacher2);

		Mockito.when(teacherDao.getAll()).thenReturn(expected);
		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacher_whenUpdate_thenCallDaoMethod() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher expected = Teacher.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Teacher oldValue = Teacher.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1991-01-01", formatter)).email("100@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();;

		Mockito.when(teacherDao.getById(1)).thenReturn(oldValue);
		

		teacherService.update(expected);
		Mockito.verify(teacherDao).update(expected);
	

	}
	
	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher student = Teacher.builder().firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", formatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		
		Mockito.when(teacherDao.getById(1)).thenReturn(student);
		
		teacherService.deleteById(1);
		
		Mockito.verify(teacherDao).deleteById(1);
	}
	
	@Test
	void givenExistId_whenDeleteById_thenNoAction() {
	
		teacherService.deleteById(1);
		
		Mockito.verify(teacherDao, Mockito.never()).deleteById(1);
	}
}
