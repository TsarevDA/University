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

		Group group = new Group.GroupBuilder().setId(1).setName("T7-09").build();
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().setId(1).setName("Name").setCapacity(100).build();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Course course = new Course.CourseBuilder().setId(1).setName("Astronomy")
				.setDescription("Science about stars and deep space").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().setId(1).setFirstName("Petr").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.setEmail("mail11111@mail.ru").setPhone("880899908080").setAddress("Petrov street, 25-5")
				.setCourses(teacherCourses).build();

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().setId(1).setOrderNumber(2).setStartTime(startTime)
				.setEndTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2020-12-08", dateFormatter);
		Lesson expected = new Lesson.LessonBuilder().setCourse(course).setTeacher(teacher).setGroup(groups).setDay(day)
				.setTime(lessonTime).setAuditorium(auditorium).build();
		
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
		Group group = new Group.GroupBuilder().setId(1).setName("T7-09").build();
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().setId(1).setName("First").setCapacity(100).build();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Course course = new Course.CourseBuilder().setId(1).setName("Astronomy")
				.setDescription("Science about stars and deep space").build();

		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().setId(1).setFirstName("Petr").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.setEmail("mail11111@mail.ru").setPhone("880899908080").setAddress("Petrov street, 25-5")
				.setCourses(teacherCourses).build();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().setId(1).setOrderNumber(2).setStartTime(startTime)
				.setEndTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2020-12-08", dateFormatter);
		Lesson expected = new Lesson.LessonBuilder().setId(1).setCourse(course).setTeacher(teacher).setGroup(groups)
				.setDay(day).setTime(lessonTime).setAuditorium(auditorium).build();
		
		Lesson actual = lessonDao.getById(1);
		
		assertEquals(expected, actual);
	}

	@Test
	@DirtiesContext
	void givenLesson_whenGetAll_thenLessonsListFound() {
		Group group = new Group.GroupBuilder().setId(1).setName("T7-09").build();
		Auditorium auditorium = new Auditorium.AuditoriumBuilder().setId(1).setName("First").setCapacity(100).build();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Course course = new Course.CourseBuilder().setId(1).setName("Astronomy")
				.setDescription("Science about stars and deep space").build();
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		Teacher teacher = new Teacher.TeacherBuilder().setId(1).setFirstName("Petr").setLastName("Ivanov")
				.setGender(Gender.valueOf("MALE")).setBirthDate(LocalDate.parse("1992-05-03", dateFormatter))
				.setEmail("mail11111@mail.ru").setPhone("880899908080").setAddress("Petrov street, 25-5")
				.setCourses(teacherCourses).build();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("09:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("10:00", timeFormatter);
		LessonTime lessonTime = new LessonTime.LessonTimeBuilder().setId(1).setOrderNumber(2).setStartTime(startTime)
				.setEndTime(endTime).build();
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);
		LocalDate day = LocalDate.parse("2020-12-08", dateFormatter);
		Lesson lesson1 = new Lesson.LessonBuilder().setId(1).setCourse(course).setTeacher(teacher).setGroup(groups).setDay(day)
				.setTime(lessonTime).setAuditorium(auditorium).build();
		List<Lesson> expected = new ArrayList<>();
		expected.add(lesson1);
	
		List<Lesson> actual = lessonDao.getAll();
		
		assertEquals(expected, actual);
	}
}
