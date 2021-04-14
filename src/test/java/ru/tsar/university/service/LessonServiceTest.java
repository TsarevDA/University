package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.config.SpringTestConfig;
import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.LessonDao;
import ru.tsar.university.dao.TeacherDao;
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
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson expected = Lesson.builder().course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		lessonService.create(expected);

		Mockito.verify(lessonDao).create(expected);
	}

	
	@Test
	void givenNewLessonAuditoriumBusy_whenCreate_thenNoAction() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson expected = Lesson.builder().course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();
		
		Group group2 = new Group.GroupBuilder().id(2).name("A7-03").build();
		Student student3 = Student.builder().id(2).firstName("Michail").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student4 = Student.builder().id(2).firstName("Silvestr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students2 = new ArrayList<>();
		students.add(student3);
		students.add(student4);
		group.setStudents(students2);
		
		Course course2 = new Course.CourseBuilder().id(2).name("Math")
				.description("Science about plants").build();
		List<Course> teacherCourses2 = new ArrayList<>();
		teacherCourses.add(course2);
		Teacher teacher2 = new Teacher.TeacherBuilder().id(2).firstName("Arnold").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses2)
				.build();
		LocalTime startTime2 = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime2 = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime2 = new LessonTime.LessonTimeBuilder().id(2).orderNumber(2).startTime(startTime2)
				.endTime(endTime2).build();
		ArrayList<Group> groups2 = new ArrayList<>();
		groups.add(group);
		LocalDate day2 = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson lesson2 = Lesson.builder().id(1).course(course2).teacher(teacher2).group(groups2).day(day2).time(lessonTime2)
				.auditorium(auditorium).build();
		List<Lesson> lessonList = new ArrayList<>();
		lessonList.add(lesson2);
		
		Mockito.when(lessonDao.getByDayTime(expected)).thenReturn(lessonList);
		
		lessonService.create(expected);

		Mockito.verify(lessonDao, Mockito.never()).create(expected);
	}
	
	@Test
	void givenNewLessonGroupBusy_whenCreate_thenNoAction() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson expected = Lesson.builder().course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();
		
		Group group2 = new Group.GroupBuilder().id(2).name("A7-03").build();
		Student student3 = Student.builder().id(2).firstName("Michail").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student4 = Student.builder().id(2).firstName("Silvestr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students2 = new ArrayList<>();
		students.add(student3);
		students.add(student4);
		group.setStudents(students2);
		Auditorium auditorium2 = new Auditorium.AuditoriumBuilder().id(2).name("A100").capacity(100).build();
		
		Course course2 = new Course.CourseBuilder().id(2).name("Math")
				.description("Science about plants").build();
		List<Course> teacherCourses2 = new ArrayList<>();
		teacherCourses.add(course2);
		Teacher teacher2 = new Teacher.TeacherBuilder().id(2).firstName("Arnold").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses2)
				.build();
		LocalTime startTime2 = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime2 = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime2 = new LessonTime.LessonTimeBuilder().id(2).orderNumber(2).startTime(startTime2)
				.endTime(endTime2).build();
		ArrayList<Group> groups2 = new ArrayList<>();
		groups.add(group);
		LocalDate day2 = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson lesson2 = Lesson.builder().id(1).course(course2).teacher(teacher2).group(groups).day(day2).time(lessonTime2)
				.auditorium(auditorium2).build();
		List<Lesson> lessonList = new ArrayList<>();
		lessonList.add(lesson2);
		
		Mockito.when(lessonDao.getByDayTime(expected)).thenReturn(lessonList);
		
		lessonService.create(expected);

		Mockito.verify(lessonDao, Mockito.never()).create(expected);
	}
	
	@Test
	void givenNewLessonTeacherNotCompetence_whenCreate_thenNoAction() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson expected = Lesson.builder().course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		lessonService.create(expected);

		Mockito.verify(lessonDao, Mockito.never()).create(expected);
	}

	@Test
	void givenId_whenGetById_thenLessonFound() {
		Group group = new Group.GroupBuilder().id(1).name("T7-09").build();
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("First").capacity(100).build();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Course course = new Course.CourseBuilder().id(1).name("Astronomy")
				.description("Science about stars and deep space").build();

		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2020-12-08", dateFormatter);
		Lesson expected = Lesson.builder().id(1).course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		
		Mockito.when(lessonDao.getById(1)).thenReturn(expected);
		Lesson actual = lessonService.getById(1);

		assertEquals(expected, actual);
	}
	
	@Test
	void givenNewLessonDayOff_whenCreate_thenNoAction() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-04-17", dateFormatter);
		Lesson expected = Lesson.builder().course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		lessonService.create(expected);

		Mockito.verify(lessonDao, Mockito.never()).create(expected);
	}

	@Test
	void givenLesson_whenGetAll_thenLessonsListFound() {
		Group group = new Group.GroupBuilder().id(1).name("T7-09").build();
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("First").capacity(100).build();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Course course = new Course.CourseBuilder().id(1).name("Astronomy")
				.description("Science about stars and deep space").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2020-12-08", dateFormatter);
		Lesson lesson1 = Lesson.builder().id(1).course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();
		List<Lesson> expected = new ArrayList<>();
		expected.add(lesson1);
		
		Mockito.when(lessonDao.getAll()).thenReturn(expected);

		List<Lesson> actual = lessonService.getAll();

		assertEquals(expected, actual);
	}
	
	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson lesson = Lesson.builder().id(1).course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();
		
		Mockito.when(lessonDao.getById(1)).thenReturn(lesson);
		
		lessonService.deleteById(1);

		Mockito.verify(lessonDao).deleteById(1);
	}
	
	@Test
	void givenNewLesson_whenUpdate_thenCallDaoMethod() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson expected = Lesson.builder().id(1).course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		Mockito.when(lessonDao.getById(1)).thenReturn(expected);
		
		lessonService.update(expected);

		Mockito.verify(lessonDao).update(expected);
	}

	
	@Test
	void givenNewLessonAuditoriumBusy_whenUpdate_thenNoAction() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson expected = Lesson.builder().id(1).course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();
		
		Group group2 = new Group.GroupBuilder().id(2).name("A7-03").build();
		Student student3 = Student.builder().id(2).firstName("Michail").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student4 = Student.builder().id(2).firstName("Silvestr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students2 = new ArrayList<>();
		students.add(student3);
		students.add(student4);
		group.setStudents(students2);
		
		Course course2 = new Course.CourseBuilder().id(2).name("Math")
				.description("Science about plants").build();
		List<Course> teacherCourses2 = new ArrayList<>();
		teacherCourses.add(course2);
		Teacher teacher2 = new Teacher.TeacherBuilder().id(2).firstName("Arnold").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses2)
				.build();
		LocalTime startTime2 = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime2 = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime2 = new LessonTime.LessonTimeBuilder().id(2).orderNumber(2).startTime(startTime2)
				.endTime(endTime2).build();
		ArrayList<Group> groups2 = new ArrayList<>();
		groups.add(group);
		LocalDate day2 = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson lesson2 = Lesson.builder().id(2).course(course2).teacher(teacher2).group(groups2).day(day2).time(lessonTime2)
				.auditorium(auditorium).build();
		List<Lesson> lessonList = new ArrayList<>();
		lessonList.add(lesson2);
		
		Mockito.when(lessonDao.getById(1)).thenReturn(expected);
		
		Mockito.when(lessonDao.getByDayTime(expected)).thenReturn(lessonList);
		
		lessonService.update(expected);

		Mockito.verify(lessonDao, Mockito.never()).update(expected);
	}
	
	@Test
	void givenNewLessonGroupBusy_whenUpdate_thenNoAction() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson expected = Lesson.builder().id(1).course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();
		
		Group group2 = new Group.GroupBuilder().id(2).name("A7-03").build();
		Student student3 = Student.builder().id(2).firstName("Michail").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student4 = Student.builder().id(2).firstName("Silvestr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students2 = new ArrayList<>();
		students.add(student3);
		students.add(student4);
		group.setStudents(students2);
		Auditorium auditorium2 = new Auditorium.AuditoriumBuilder().id(2).name("A100").capacity(100).build();
		
		Course course2 = new Course.CourseBuilder().id(2).name("Math")
				.description("Science about plants").build();
		List<Course> teacherCourses2 = new ArrayList<>();
		teacherCourses.add(course2);
		Teacher teacher2 = new Teacher.TeacherBuilder().id(2).firstName("Arnold").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses2)
				.build();
		LocalTime startTime2 = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime2 = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime2 = new LessonTime.LessonTimeBuilder().id(2).orderNumber(2).startTime(startTime2)
				.endTime(endTime2).build();
		ArrayList<Group> groups2 = new ArrayList<>();
		groups.add(group);
		LocalDate day2 = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson lesson2 = Lesson.builder().id(2).course(course2).teacher(teacher2).group(groups).day(day2).time(lessonTime2)
				.auditorium(auditorium2).build();
		List<Lesson> lessonList = new ArrayList<>();
		lessonList.add(lesson2);
		
		Mockito.when(lessonDao.getById(1)).thenReturn(expected);
		Mockito.when(lessonDao.getByDayTime(expected)).thenReturn(lessonList);
		
		lessonService.update(expected);

		Mockito.verify(lessonDao, Mockito.never()).update(expected);
	}
	
	@Test
	void givenNewLessonTeacherNotCompetence_whenUpdate_thenNoAction() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-12-08", dateFormatter);
		Lesson expected = Lesson.builder().id(1).course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		Mockito.when(lessonDao.getById(1)).thenReturn(expected);
		lessonService.update(expected);

		Mockito.verify(lessonDao, Mockito.never()).update(expected);
	}

	@Test
	void givenNewLessonDayOff_whenUpdate_thenNoAction() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Group group = new Group.GroupBuilder().id(1).name("A5-03").build();
		Student student1 = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail@mail.ru").phone("88008080")
				.address("Ivanov street, 25-5").build();
		Student student2 = Student.builder().id(1).firstName("Petr").lastName("Petrov").gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.parse("1990-01-01", dateFormatter)).email("mail111@mail.ru").phone("88008080")
				.address("Petrov street, 25-5").build();
		ArrayList<Student> students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		group.setStudents(students);
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("A1000").capacity(100).build();
		
		Course course = new Course.CourseBuilder().id(1).name("Biology")
				.description("Science about plants").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().id(1).firstName("Petr").lastName("Ivanov")
				.gender(Gender.valueOf("MALE")).birthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.email("mail11111@mail.ru").phone("880899908080").address("Petrov street, 25-5").courses(teacherCourses)
				.build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().id(1).orderNumber(2).startTime(startTime)
				.endTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2021-04-17", dateFormatter);
		Lesson expected = Lesson.builder().id(1).course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		Mockito.when(lessonDao.getById(1)).thenReturn(expected);
		
		lessonService.update(expected);

		Mockito.verify(lessonDao, Mockito.never()).update(expected);
	}
	
	
	@Test
	void givenId_whenDeleteById_thenNoAction() {
		
		lessonService.deleteById(1);

		Mockito.verify(lessonDao, Mockito.never()).deleteById(1);
	}
}
