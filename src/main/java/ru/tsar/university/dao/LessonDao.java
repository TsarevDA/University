package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Teacher;

@Component
public class LessonDao {

	final static private String CREATE_LESSON_QUERY = "INSERT INTO lessons(course_id,teacher_id,day,lesson_time,auditorium) VALUES(?,?,?,?,?)";
	final static private String CREATE_LESSONS_GROUPS_QUERY = "INSERT INTO lessons_groups(lesson_id,group_id) VALUES(?,?)";
	final static private String DELETE_LESSONS_GROUPS_QUERY = "DELETE FROM lessons_groups where lesson_id =?";
	final static private String DELETE_LESSON_QUERY = "DELETE FROM lessons where id =?";
	final static private String GET_BY_ID_REQUEST = "SELECT l.id as lesson_id,l.day,"
			+ "c.id as course_id,c.course_name, c.description, "
			+ "t.id as teacher_id, t.first_name, t.last_name, t.gender, t.birth_date, t.email, t.phone, t.address, "
			+ "lt.id as lesson_time_id, lt.order_number, lt.start_time, lt.end_time, "
			+ "a.id as auditorium_id, a.auditorium_name,a.capacity "
			+ "FROM lessons l left join courses c on l.course_id=c.id " + "left join teachers t on l.teacher_id=t.id "
			+ "left join lessons_time lt on lt.order_number = l.lesson_time "
			+ "left join auditoriums a on l.auditorium= a.id WHERE l.id =?";
	final static private String GET_TEACHER_COURSES_REQUEST = "SELECT c.* FROM teachers_courses tc left join courses c on tc.course_id = c.id WHERE teacher_id = ?";
	final static private String GET_GROUPS_LESSONS_REQUEST = "SELECT g.* FROM lessons_groups lg left join groups g on lg.group_id = g.id WHERE group_id = ?";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public LessonDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void create(Lesson lesson) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_LESSON_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, lesson.getCourse().getId());
			ps.setInt(2, lesson.getTeacher().getId());
			ps.setDate(3, java.sql.Date.valueOf(lesson.getDay()));
			ps.setInt(4, lesson.getTime().getOrderNumber());
			ps.setInt(5, lesson.getAuditorium().getId());
			return ps;
		}, holder);
		lesson.setId((int) holder.getKeys().get("id"));

		lesson.getGroup().stream()
				.forEach(g -> jdbcTemplate.update(CREATE_LESSONS_GROUPS_QUERY, lesson.getId(), g.getId()));
	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_LESSONS_GROUPS_QUERY, id);
		jdbcTemplate.update(DELETE_LESSON_QUERY, id);
	}

	public Lesson getById(int id) {

		List<Course> coursesForTeacher = jdbcTemplate.query(GET_TEACHER_COURSES_REQUEST, (resultSet, rowNum) -> {
			Course course = new Course(resultSet.getInt("id"), resultSet.getString("course_name"),
					resultSet.getString("description"));
			return course;
		}, id);

		List<Group> groups = jdbcTemplate.query(GET_GROUPS_LESSONS_REQUEST, (resultSet, rowNum) -> {
			Group group = new Group(resultSet.getInt("id"), resultSet.getString("group_name"));
			return group;
		}, id);

		Lesson lesson = jdbcTemplate.queryForObject(GET_BY_ID_REQUEST, (resultSet, rowNum) -> {
			Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"),
					resultSet.getString("description"));

			Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("first_name"),
					resultSet.getString("last_name"), Gender.valueOf(resultSet.getString("gender")),
					resultSet.getDate("birth_date").toLocalDate(), resultSet.getString("email"),
					resultSet.getString("phone"), resultSet.getString("address"), coursesForTeacher);

			LessonTime lessonTime = new LessonTime(resultSet.getInt("lesson_time_id"), resultSet.getInt("order_number"),
					resultSet.getTime("start_time").toLocalTime(), resultSet.getTime("end_time").toLocalTime());

			Auditorium auditorium = new Auditorium(resultSet.getInt("auditorium_id"),
					resultSet.getString("auditorium_name"), resultSet.getInt("capacity"));

			Lesson newLesson = new Lesson(id, course, teacher, groups, resultSet.getDate("day").toLocalDate(),
					lessonTime, auditorium);
			return newLesson;
		}, id);

		return lesson;
	}

}
