package ru.tsar.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Course;

@Component
public class CourseRowMapper implements RowMapper<Course> {

	@Override
	public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
		return Course.builder().id(rs.getInt("id")).name(rs.getString("name")).description(rs.getString("description")).build();
	}
}