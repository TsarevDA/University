package ru.tsar.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.LessonTime;

@Component
public class LessonTimeRowMapper implements RowMapper<LessonTime> {

	@Override
	public LessonTime mapRow(ResultSet rs, int rowNum) throws SQLException {
		LessonTime lessonTime = new LessonTime(rs.getInt("id"), rs.getInt("order_number"),
				rs.getTime("start_time").toLocalTime(), rs.getTime("end_time").toLocalTime());
		return lessonTime;
	}

}
