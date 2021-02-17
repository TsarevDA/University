package ru.tsar.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.LessonTime;

@Component
public class LessonTimeRowMapper implements RowMapper<LessonTime> {

	@Override
	public LessonTime mapRow(ResultSet rs, int rowNum) throws SQLException {
		LessonTime lessonTime = new LessonTime(rs.getInt("id"), rs.getInt("order_number"),
				rs.getObject("start_time",LocalTime.class),rs.getObject("end_time",LocalTime.class));
		return lessonTime;
	}

}
