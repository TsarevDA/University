package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static ru.tsar.university.service.LessonServiceTest.TestData.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.LessonDao;
import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.exceptions.AuditoriumFreeException;
import ru.tsar.university.exceptions.CapacityEnoughException;
import ru.tsar.university.exceptions.DayOffException;
import ru.tsar.university.exceptions.GroupFreeException;
import ru.tsar.university.exceptions.LessonExistException;
import ru.tsar.university.exceptions.TeacherCompetentException;
import ru.tsar.university.exceptions.TeacherFreeException;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(LessonServiceTest.class);
	@InjectMocks
	private LessonService lessonService;
	@Mock
	private LessonDao lessonDao;
	@Mock
	private AuditoriumDao auditoriumDao;
	@Mock
	private TeacherDao teacherDao;
	@Mock
	private GroupDao groupDao;

	@Test
	void givenNewLesson_whenCreate_thenCallDaoMethod() {

		Lesson expected = Lesson.builder()
				.course(course_1)
				.teacher(teacher_1)
				.group(groups_1)
				.day(day_1)
				.time(lessonTime_1)
				.auditorium(auditorium_1)
				.build();

		lessonService.create(expected);
		
		verify(lessonDao).create(expected);
	}

	@Test
	void givenNewLessonAuditoriumBusy_whenCreate_thenNoAction() {
		Lesson lesson1 = Lesson.builder()
				.course(course_1)
				.teacher(teacher_1)
				.group(groups_1)
				.day(day_1)
				.time(lessonTime_1)
				.auditorium(auditorium_1)
				.build();

		//when(lessonDao.getByDayTimeAuditorium(lesson1.getDay(), lesson1.getTime(), lesson1.getAuditorium()))
		//		.thenReturn(lessonSameAuditorium);

		verify(lessonDao, never()).create(lesson1);
	}

	@Test
	void givenNewLessonGroupBusy_whenCreate_thenNoAction() {
		Lesson lesson1 = Lesson.builder()
				.course(course_1)
				.teacher(teacher_1)
				.group(groups_1)
				.day(day_1)
				.time(lessonTime_1)
				.auditorium(auditorium_1)
				.build();
		Lesson lesson2 = lessonSameGroup;
		List<Lesson> lessonList = new ArrayList<>();
		lessonList.add(lesson2);

		//when(lessonDao.getByDayTime(lesson1.getDay(), lesson1.getTime())).thenReturn(lessonList);

		verify(lessonDao, never()).create(lesson1);
	}

	@Test
	void givenNewLessonTeacherNotCompetence_whenCreate_thenNoAction() {
		Lesson lesson1 = Lesson.builder()
				.course(course_1)
				.teacher(teacher_2)
				.group(groups_1)
				.day(day_1)
				.time(lessonTime_1)
				.auditorium(auditorium_1)
				.build();

		verify(lessonDao, never()).create(lesson1);
	}
	
	@Test
	void givenNewLessonDayOff_whenCreate_thenNoAction() {
		Lesson lesson1 = Lesson.builder()
				.course(course_1)
				.teacher(teacher_2)
				.group(groups_1)
				.day(dayOff)
				.time(lessonTime_1)
				.auditorium(auditorium_1)
				.build();
		
		lessonService.create(lesson1);

		verify(lessonDao, never()).create(lesson1);
	}

	@Test
	void givenId_whenGetById_thenLessonFound() {

		Lesson expected = lesson_1;

		when(lessonDao.getById(1)).thenReturn(expected);
		Lesson actual = lessonService.getById(1);

		assertEquals(expected, actual);
	}



	@Test
	void givenLesson_whenGetAll_thenLessonsListFound() {
		Lesson lesson1 = lesson_1;
		List<Lesson> expected = new ArrayList<>();
		expected.add(lesson1);

		when(lessonDao.getAll()).thenReturn(expected);

		List<Lesson> actual = lessonService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		Lesson lesson = lesson_1;

		when(lessonDao.getById(1)).thenReturn(lesson);

		lessonService.deleteById(1);

		verify(lessonDao).deleteById(1);
	}

	@Test
	void givenNewLesson_whenUpdate_thenCallDaoMethod() {
		Lesson expected = lesson_1;

		when(lessonDao.getById(1)).thenReturn(expected);

		lessonService.update(expected);
			
		verify(lessonDao).update(expected);
	}

	@Test
	void givenNewLessonAuditoriumBusy_whenUpdate_thenNoAction() {
		Lesson expected = lesson_1;
		
		when(lessonDao.getById(1)).thenReturn(expected);

		when(lessonDao.getByDayTimeAuditorium(expected.getDay(), expected.getTime(), expected.getAuditorium()))
				.thenReturn(lessonSameAuditorium);
		
		lessonService.update(expected);

		verify(lessonDao, never()).update(expected);
	}

	@Test
	void givenNewLessonGroupBusy_whenUpdate_thenNoAction() {
		Lesson expected = lesson_1;
		Lesson lesson2 = lessonSameGroup;
		List<Lesson> lessonList = new ArrayList<>();
		lessonList.add(lesson2);

		when(lessonDao.getById(1)).thenReturn(lesson_1);
		when(lessonDao.getByDayTime(expected.getDay(), expected.getTime())).thenReturn(lessonList);

		
		lessonService.update(expected);

		verify(lessonDao, never()).update(expected);
	}

	@Test
	void givenNewLessonTeacherNotCompetence_whenUpdate_thenNoAction() {
		Lesson expected = lessonNoCompetenceTeacher;

		when(lessonDao.getById(2)).thenReturn(lessonNoCompetenceTeacher);
	
		lessonService.update(expected);
		

		verify(lessonDao, never()).update(expected);
	}

	@Test
	void givenNewLessonDayOff_whenUpdate_thenNoAction() {
		Lesson expected = lessonDayOff;

		when(lessonDao.getById(1)).thenReturn(lesson_1);
		
		lessonService.update(expected);
		

		verify(lessonDao, never()).update(expected);
	}

	@Test
	void givenId_whenDeleteById_thenNoAction() {

		lessonService.deleteById(1);

		verify(lessonDao, never()).deleteById(1);
	}

	interface TestData {
		Student student_1 = Student.builder()
				.id(1).firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5")
				.build();
		Student student_2 = Student.builder()
				.id(1)
				.firstName("Petr")
				.lastName("Petrov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail111@mail.ru")
				.phone("88008080")
				.address("Petrov street, 25-5")
				.build();
		Student student_3 = Student.builder()
				.id(3)
				.firstName("Michail")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.build();
		Student student_4 = Student.builder()
				.id(4)
				.firstName("Silvestr")
				.lastName("Petrov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1990, Month.JANUARY, 1))
				.email("mail111@mail.ru")
				.phone("88008080")
				.address("Petrov street, 25-5")
				.build();
		
		List<Student> students_1 = Arrays.asList(student_1, student_2);
		List<Student> students_2 = Arrays.asList(student_3, student_4);

		Group group_1 = Group.builder()
				.id(1)
				.name("A5-03")
				.students(students_1)
				.build();
		Group group_2 = Group.builder()
				.id(2)
				.name("A7-03")
				.students(students_2)
				.build();
		List<Group> groups_1 = Arrays.asList(group_1);
		List<Group> groups_2 = Arrays.asList(group_2);

		Auditorium auditorium_1 = Auditorium.builder()
				.id(1)
				.name("A1000")
				.capacity(1000)
				.build();
		Auditorium auditorium_2 = Auditorium.builder()
				.id(2)
				.name("A100")
				.capacity(100)
				.build();
		
		Course course_1 = Course.builder()
				.id(1)
				.name("Biology")
				.description("Science about plants")
				.build();
		Course course_2 = Course.builder()
				.id(2)
				.name("Math")
				.description("Science about digits")
				.build();

		List<Course> teacherCourses_1 = Arrays.asList(course_1);
		List<Course> teacherCourses_2 = Arrays.asList(course_2);

		Teacher teacher_1 = Teacher.builder()
				.id(1)
				.firstName("Petr")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1992, Month.MAY, 03))
				.email("mail11111@mail.ru")
				.phone("880899908080")
				.address("Petrov street, 25-5")
				.courses(teacherCourses_1)
				.build();
		Teacher teacher_2 = Teacher.builder()
				.id(2)
				.firstName("Arnold")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1992, Month.MAY, 03))
				.email("mail11111@mail.ru")
				.phone("880899908080")
				.address("Petrov street, 25-5")
				.courses(teacherCourses_2)
				.build();
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
				.id(2)
				.orderNumber(2)
				.startTime(startTime_2)
				.endTime(endTime_2)
				.build();

		LocalDate day_1 = LocalDate.of(2021, Month.DECEMBER, 8);
		LocalDate day_2 = LocalDate.of(2021, Month.JANUARY, 11);
		LocalDate dayOff = LocalDate.of(2021, Month.APRIL, 17);

		Lesson lesson_1 = Lesson.builder()
				.id(1)
				.course(course_1)
				.teacher(teacher_1)
				.group(groups_1)
				.day(day_1)
				.time(lessonTime_1)
				.auditorium(auditorium_1)
				.build();
		Lesson lessonSameAuditorium = Lesson.builder()
				.id(2)
				.course(course_2)
				.teacher(teacher_2)
				.group(groups_2)
				.day(day_1)
				.time(lessonTime_1)
				.auditorium(auditorium_1)
				.build();
		Lesson lessonSameGroup = Lesson.builder()
				.id(2)
				.course(course_2)
				.teacher(teacher_2)
				.group(groups_1)
				.day(day_1)
				.time(lessonTime_1)
				.auditorium(auditorium_2)
				.build();
		Lesson lessonNoCompetenceTeacher = Lesson.builder()
				.id(2)
				.course(course_1)
				.teacher(teacher_2)
				.group(groups_1)
				.day(day_1)
				.time(lessonTime_1)
				.auditorium(auditorium_2)
				.build();
		Lesson lessonDayOff = Lesson.builder()
				.id(1)
				.course(course_1)
				.teacher(teacher_1)
				.group(groups_1)
				.day(dayOff)
				.time(lessonTime_1)
				.auditorium(auditorium_2)
				.build();
	}
}
