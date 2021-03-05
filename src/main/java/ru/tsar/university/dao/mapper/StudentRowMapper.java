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
		return new Student.StudentBuilder().setId(rs.getInt("id")).setFirstName(rs.getString("first_name"))
		.setLastName(rs.getString("last_name")).setGender(Gender.valueOf(rs.getString("gender")))
		.setBirthDate(rs.getObject("birth_date", LocalDate.class)).setEmail(rs.getString("email"))
		.setPhone(rs.getString("phone")).setAddress(rs.getString("address")).build();
	}

}
