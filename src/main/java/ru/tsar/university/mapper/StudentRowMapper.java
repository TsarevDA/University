package ru.tsar.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;


public class StudentRowMapper implements RowMapper<Student> {

	@Override
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		Student student = new Student(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
				Gender.valueOf(rs.getString("gender")), rs.getDate("birth_date").toLocalDate(),
				rs.getString("email"), rs.getString("phone"), rs.getString("address"));
		return student;
	}

}

