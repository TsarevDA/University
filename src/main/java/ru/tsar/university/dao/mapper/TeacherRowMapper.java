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
		Teacher teacher = new Teacher(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
				Gender.valueOf(rs.getString("gender")), rs.getObject("birth_date", LocalDate.class),
				rs.getString("email"), rs.getString("phone"), rs.getString("address"));
		teacher.setCourses(courseDao.getByTeacher(teacher));
		return teacher;
	}

}
