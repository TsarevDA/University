package ru.tsar.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.model.Gender;

import ru.tsar.university.model.Teacher;

@Component
public class TeacherRowMapper implements RowMapper<Teacher> {

	private CourseDao courseDao;

	public TeacherRowMapper(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@Override
	public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Teacher.TeacherBuilder().setId(rs.getInt("id")).setFirstName(rs.getString("first_name"))
				.setLastName(rs.getString("last_name")).setGender(Gender.valueOf(rs.getString("gender")))
				.setBirthDate(rs.getObject("birth_date", LocalDate.class)).setEmail(rs.getString("email"))
				.setPhone(rs.getString("phone")).setAddress(rs.getString("address")).setCourses(courseDao.getByTeacherId(rs.getInt("id"))).build();
	}

}
