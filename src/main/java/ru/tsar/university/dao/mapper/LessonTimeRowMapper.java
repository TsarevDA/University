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
		return LessonTime.builder().id(rs.getInt("id"))
				.orderNumber(rs.getInt("order_number")).startTime(rs.getObject("start_time", LocalTime.class))
				.endTime(rs.getObject("end_time", LocalTime.class)).build();
	}

}
