package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ru.tsar.university.SpringConfig;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Teacher;

@SpringJUnitConfig(classes = SpringConfig.class)
@Sql("/schema.sql")
class LessonDaoTest {

	final static private String GET_COUNT_BY_ID_REQUEST = "select count(*) FROM lessons WHERE id=?";
	final static private String GET_BY_ID_QUERY = "SELECT l.id as lesson_id,l.day,"
			+ "c.id as course_id,c.name as course_name, c.description, "
			+ "t.id as teacher_id, t.first_name, t.last_name, t.gender, t.birth_date, t.email, t.phone, t.address, "
			+ "lt.id as lesson_time_id, lt.order_number, lt.start_time, lt.end_time, "
			+ "a.id as auditorium_id, a.name as auditorium_name,a.capacity "
			+ "FROM lessons l left join courses c on l.course_id=c.id " + "left join teachers t on l.teacher_id=t.id "
			+ "left join lessons_time lt on lt.order_number = l.lesson_time "
			+ "left join auditoriums a on l.auditorium= a.id WHERE l.id =?";
	final static private String GET_TEACHER_COURSES_REQUEST = "SELECT c.* FROM teachers_courses tc left join courses c on tc.course_id = c.id WHERE teacher_id = ?";
	final static private String GET_GROUPS_LESSONS_REQUEST = "SELECT g.* FROM lessons_groups lg left join groups g on lg.group_id = g.id WHERE group_id = ?";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LessonDao lessonDao;
	@Autowired
	private AuditoriumDao auditoriumDao;
	@Autowired
	private CourseDao courseDao;
	@Autowired
	private TeacherDao teacherDao;
	@Autowired
	private LessonTimeDao lessonTimeDao;
	@Autowired
	private GroupDao groupDao;

	@Test
	void setLesson_whenCreate_thenCreateLesson() {
		Group group = new Group("T7-09");
		Auditorium auditorium = new Auditorium("Name", 100);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Course course = new Course("Astronomy", "Science about stars and deep space");
		Teacher teacher = new Teacher("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", dateFormatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		teacher.setCourses(teacherCourses);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("08:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("09:00", timeFormatter);
		LessonTime lessonTime = new LessonTime(1, startTime, endTime);
		groupDao.create(group);
		auditoriumDao.create(auditorium);
		courseDao.create(course);
		teacherDao.create(teacher);
		lessonTimeDao.create(lessonTime);
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);

		LocalDate day = LocalDate.parse("2020-12-08", dateFormatter);
		Lesson expected = new Lesson(course, teacher, groups, day, lessonTime, auditorium);
		lessonDao.create(expected);

		List<Course> coursesForTeacher = jdbcTemplate.query(GET_TEACHER_COURSES_REQUEST, (resultSet, rowNum) -> {
			Course newCourse = new Course(resultSet.getInt("id"), resultSet.getString("name"),
					resultSet.getString("description"));
			return newCourse;
		}, expected.getId());

		List<Group> actualGroups = jdbcTemplate.query(GET_GROUPS_LESSONS_REQUEST, (resultSet, rowNum) -> {
			Group newGroup = new Group(resultSet.getInt("id"), resultSet.getString("name"));
			return newGroup;
		}, expected.getId());

		Lesson actual = jdbcTemplate.queryForObject(GET_BY_ID_QUERY, (resultSet, rowNum) -> {
			Course actualCourse = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"),
					resultSet.getString("description"));

			Teacher actualTeacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("first_name"),
					resultSet.getString("last_name"), Gender.valueOf(resultSet.getString("gender")),
					resultSet.getDate("birth_date").toLocalDate(), resultSet.getString("email"),
					resultSet.getString("phone"), resultSet.getString("address"), coursesForTeacher);

			LessonTime actualLessonTime = new LessonTime(resultSet.getInt("lesson_time_id"),
					resultSet.getInt("order_number"), resultSet.getTime("start_time").toLocalTime(),
					resultSet.getTime("end_time").toLocalTime());

			Auditorium actualAuditorium = new Auditorium(resultSet.getInt("auditorium_id"),
					resultSet.getString("auditorium_name"), resultSet.getInt("capacity"));

			Lesson newLesson = new Lesson(resultSet.getInt("lesson_id"), actualCourse, actualTeacher, groups,
					resultSet.getDate("day").toLocalDate(), actualLessonTime, actualAuditorium);
			return newLesson;
		}, expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteGroup() {
		Group group = new Group("T7-09");
		Auditorium auditorium = new Auditorium("Name", 100);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Course course = new Course("Astronomy", "Science about stars and deep space");
		Teacher teacher = new Teacher("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", dateFormatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		teacher.setCourses(teacherCourses);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("08:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("09:00", timeFormatter);
		LessonTime lessonTime = new LessonTime(1, startTime, endTime);
		groupDao.create(group);
		auditoriumDao.create(auditorium);
		courseDao.create(course);
		teacherDao.create(teacher);
		lessonTimeDao.create(lessonTime);
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);

		LocalDate day = LocalDate.parse("2020-12-08", dateFormatter);
		Lesson lesson = new Lesson(course, teacher, groups, day, lessonTime, auditorium);
		lessonDao.create(lesson);
		lessonDao.deleteById(lesson.getId());

		int actual = jdbcTemplate.queryForObject(GET_COUNT_BY_ID_REQUEST, Integer.class, lesson.getId());
		assertEquals(0, actual);
	}

	@Test
	void setId_whenGetById_thenGetLesson() {
		Group group = new Group("T7-09");
		Auditorium auditorium = new Auditorium("Name", 100);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Course course = new Course("Astronomy", "Science about stars and deep space");
		Teacher teacher = new Teacher("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", dateFormatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		List<Course> teacherCourses = new ArrayList<>();
		teacherCourses.add(course);
		teacher.setCourses(teacherCourses);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = LocalTime.parse("08:00", timeFormatter);
		LocalTime endTime = LocalTime.parse("09:00", timeFormatter);
		LessonTime lessonTime = new LessonTime(1, startTime, endTime);
		groupDao.create(group);
		auditoriumDao.create(auditorium);
		courseDao.create(course);
		teacherDao.create(teacher);
		lessonTimeDao.create(lessonTime);
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(group);

		LocalDate day = LocalDate.parse("2020-12-08", dateFormatter);
		Lesson expected = new Lesson(course, teacher, groups, day, lessonTime, auditorium);
		lessonDao.create(expected);

		Lesson actual = lessonDao.getById(expected.getId());

		assertEquals(expected, actual);
	}

}
