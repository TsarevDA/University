package ru.tsar.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;

@Component
public class GroupRowMapper implements RowMapper<Group> {
	
	private StudentDao studentDao;
	
	public GroupRowMapper (StudentDao studentDao) {
		this.studentDao = studentDao;
	}
	
	@Override
	public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		List<Student> students = studentDao.getByGroupId(rs.getInt("id"));
		
		return Group.builder()
				.id(rs.getInt("id"))
				.name(rs.getString("name"))
				.students(students)
				.build();
	}
}
