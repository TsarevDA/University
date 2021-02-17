package ru.tsar.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;

@Component
public class StudentRowMapper implements RowMapper<Student> {

	@Override
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		Student student = new Student(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
				Gender.valueOf(rs.getString("gender")), rs.getObject("birth_date", LocalDate.class),
				rs.getString("email"), rs.getString("phone"), rs.getString("address"));
		return student;
	}

}
