package ru.tsar.dao;

import java.time.LocalTime;

import org.springframework.jdbc.core.JdbcTemplate;

import ru.tsar.university.model.LessonTime;

public class LessonTimeDao {

	final private String ADD_LESSON_TIME_QUERY = "INSERT INTO lessons_time(order_number,start_time,end_time) VALUES(?,?,?)";
	final private String DELETE_LESSON_TIME_QUERY = "DELETE FROM lessons_time where id =?";
	private JdbcTemplate jdbcTemplate;

	public LessonTimeDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void addLessonTime(LessonTime lessonTime) {
		jdbcTemplate.update(ADD_LESSON_TIME_QUERY, lessonTime.getOrderNumber(), lessonTime.getStartTime(),
				lessonTime.getEndTime());
	}

	public void deleteLessonTime(int id) {
		jdbcTemplate.update(DELETE_LESSON_TIME_QUERY, id);
	}

}
