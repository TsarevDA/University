package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tsar.university.dao.LessonDaoTest.TestData.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.config.SpringTestConfig;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;

@SpringJUnitConfig(SpringTestConfig.class)
@Sql("/lessonData.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@WebAppConfiguration
class LessonDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LessonDao lessonDao;

	@Test
	void givenNewLesson_whenCreate_thenCreated() {
		Lesson expected = Lesson.builder().course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		lessonDao.create(expected);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {

		lessonDao.deleteById(1);

		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons", "id = 1");
		assertEquals(0, actual);
	}

	@Test
	void givenId_whenGetById_thenLessonFound() {

		Lesson expected = Lesson.builder().id(1).course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		Lesson actual = lessonDao.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenLesson_whenGetAll_thenLessonsListFound() {
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(lesson);
		Page<Lesson> expected = new PageImpl<>(lessons, pageable, lessons.size());
		Page<Lesson> actual = lessonDao.getAll(pageable);

		assertEquals(expected, actual);
	}

	@Test
	void givenDayTime_whenGetByDayTime_thenLessonListFound() {

		List<Lesson> expected = new ArrayList<>();
		expected.add(lesson);

		List<Lesson> actual = lessonDao.getByDayTime(day, lessonTime);

		assertEquals(expected, actual);
	}

	@Test
	void givenDayTimeAuditorium_whenGetByDayTimeAuditorium_thenLessonListFound() {
		Lesson expected = lesson;
		Lesson actual = lessonDao.getByDayTimeAuditorium(day, lessonTime, auditorium);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenDayTimeTeacher_whenGetByDayTimeTeacher_thenLessonListFound() {
		Lesson expected = lesson;
		Lesson actual = lessonDao.getByDayTimeTeacher(day, lessonTime, teacher);

		assertEquals(expected, actual);
	}

	interface TestData {
		Pageable pageable = PageRequest.of(0, 5);
		List<Student> students = new ArrayList<>();
		Group group = Group.builder().id(1).name("T7-09").students(students).build();
		List<Group> groups = Arrays.asList(group);

		Auditorium auditorium = Auditorium.builder()
				.id(1)
				.name("First")
				.capacity(100)
				.build();
		Course course = Course.builder()
				.id(1).name("Astronomy")
				.description("Science about stars and deep space")
				.build();
		List<Course> teacherCourses = Arrays.asList(course);

		Teacher teacher = Teacher.builder()
				.id(1).firstName("Petr")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1992, Month.MAY, 03))
				.email("mail11111@mail.ru")
				.phone("880899908080")
				.address("Petrov street, 25-5")
				.courses(teacherCourses)
				.build();
		LocalTime startTime = LocalTime.of(9, 0);
		LocalTime endTime = LocalTime.of(10, 0);
		LessonTime lessonTime = LessonTime.builder().id(1).orderNumber(2).startTime(startTime).endTime(endTime).build();

		LocalDate day = LocalDate.of(2020, Month.DECEMBER, 8);
		Lesson lesson = Lesson.builder()
				.id(1)
				.course(course)
				.teacher(teacher)
				.group(groups)
				.day(day)
				.time(lessonTime)
				.auditorium(auditorium)
				.build();
	}
}
