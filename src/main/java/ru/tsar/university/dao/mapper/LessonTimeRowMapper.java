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
		return new LessonTime.LessonTimeBuilder().setId(rs.getInt("id"))
				.setOrderNumber(rs.getInt("order_number")).setStartTime(rs.getObject("start_time", LocalTime.class))
				.setEndTime(rs.getObject("end_time", LocalTime.class)).build();
	}

}
