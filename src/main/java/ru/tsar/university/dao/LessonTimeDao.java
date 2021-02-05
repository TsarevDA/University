package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.mapper.LessonTimeRowMapper;
import ru.tsar.university.model.LessonTime;

@Component
public class LessonTimeDao {

	private static final String ADD_LESSON_TIME_QUERY = "INSERT INTO lessons_time(order_number,start_time,end_time) VALUES(?,?,?)";
	private static final String DELETE_LESSON_TIME_QUERY = "DELETE FROM lessons_time where id =?";
	private static final String GET_BY_ORDER_NUMBER_QUERY = "SELECT * FROM lessons_time WHERE order_number=?";
	private static final String UPDATE_LESSON_TIME_QUERY = "UPDATE lessons_time SET start_time=?,end_time=?, WHERE order_number=?";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LessonTimeRowMapper rowMapper;

	public void create(LessonTime lessonTime) {

		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(ADD_LESSON_TIME_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, lessonTime.getOrderNumber());
			ps.setTime(2, Time.valueOf(lessonTime.getStartTime()));
			ps.setTime(3, Time.valueOf(lessonTime.getEndTime()));

			return ps;
		}, holder);
		lessonTime.setId((int) holder.getKeys().get("id"));

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_LESSON_TIME_QUERY, id);
	}

	public LessonTime getByOrder(int order) {
		return jdbcTemplate.queryForObject(GET_BY_ORDER_NUMBER_QUERY, rowMapper, order);
	}

	public void update(LessonTime lessonTime) {
		jdbcTemplate.update(UPDATE_LESSON_TIME_QUERY, lessonTime.getStartTime(), lessonTime.getEndTime(),
				lessonTime.getOrderNumber());
	}

}
