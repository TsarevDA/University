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
		return Student.builder().
				id(rs.getInt("id")).
				firstName(rs.getString("first_name")).
				lastName(rs.getString("last_name")).
				gender(Gender.valueOf(rs.getString("gender"))).
				birthDate(rs.getObject("birth_date", LocalDate.class)).
				email(rs.getString("email")).
				phone(rs.getString("phone")).
				address(rs.getString("address")).
				build();
	}

}
