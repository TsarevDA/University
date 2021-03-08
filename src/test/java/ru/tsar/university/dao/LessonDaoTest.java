package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.config.SpringTestConfig;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Teacher;

@SpringJUnitConfig
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql("/lessonData.sql")
class LessonDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LessonDao lessonDao;

	@Test
	@DirtiesContext
	void givenNewLesson_whenCreate_thenCreated() {

		Group group = new Group.GroupBuilder().id(1).name("T7-09").build();
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().id(1).name("Name").capacity(100).build();
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
		Lesson expected = Lesson.builder().course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		lessonDao.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	@DirtiesContext
	void givenId_whenDeleteById_thenDeleted() {

		lessonDao.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	@DirtiesContext
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

		Lesson actual = lessonDao.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	@DirtiesContext
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

		List<Lesson> actual = lessonDao.getAll();

		assertEquals(expected, actual);
	}
}
