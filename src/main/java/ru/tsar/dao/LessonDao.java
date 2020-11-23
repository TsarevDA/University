package ru.tsar.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;

public class LessonDao {

	final private String ADD_LESSON_QUERY = "INSERT INTO lessons(course_id,teacher_id,day,lesson_time,auditorium) VALUES(?,?,?,?,?)";
	final private String ADD_LESSONS_GROUPS_QUERY = "INSERT INTO lessons_groups(lesson_id,group_id) VALUES(?,?)";
	final private String DELETE_LESSONS_GROUPS_QUERY = "DELETE FROM lessons_groups where lesson_id =?";
	final private String DELETE_LESSON_QUERY = "DELETE FROM lessons where lesson_id =?";
	private JdbcTemplate jdbcTemplate;

	public LessonDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void addLesson(Lesson lesson) {
		jdbcTemplate.update(ADD_LESSON_QUERY, lesson.getCourse(), lesson.getTeacher(), lesson.getDay(),
				lesson.getTime(), lesson.getAuditorium());
		lesson.getGroup().stream()
				.forEach(s -> jdbcTemplate.update(ADD_LESSONS_GROUPS_QUERY, lesson.getId(), s.getId()));
	}

	public void deleteLesson(int id) {
		jdbcTemplate.update(DELETE_LESSONS_GROUPS_QUERY, id);
		jdbcTemplate.update(DELETE_LESSON_QUERY, id);
	}
}
