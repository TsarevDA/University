package ru.tsar.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Gender;

import ru.tsar.university.model.Teacher;

@Component
public class TeacherRowMapper implements RowMapper<Teacher> {

	@Override
	public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
		Teacher teacher = new Teacher(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
				Gender.valueOf(rs.getString("gender")), rs.getDate("birth_date").toLocalDate(), rs.getString("email"),
				rs.getString("phone"), rs.getString("address"));
		return teacher;
	}

}
