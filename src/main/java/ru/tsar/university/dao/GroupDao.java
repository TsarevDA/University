package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;

@Component
public class GroupDao {

	final static private String ADD_GROUP_QUERY = "INSERT INTO groups(group_name) VALUES(?)";
	final static private String ADD_GROUPS_STUDENTS_QUERY = "INSERT INTO groups_students(group_id,student_id) VALUES(?,?)";
	final static private String DELETE_GROUPS_STUDENTS_QUERY = "DELETE FROM groups_students where group_id =?";
	final static private String DELETE_GROUP_QUERY = "DELETE FROM groups where id =?";
	final static private String GET_BY_ID_REQUEST = "SELECT g.* FROM groups g WHERE id=?";
	final static private String GET_STUDENTS_BY_GROUP_ID_REQUEST = "SELECT gs.* FROM groups_students gs WHERE group_id=?";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public GroupDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void create(Group group) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(ADD_GROUP_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, group.getName());
			return ps;
		}, holder);
		group.setId((int) holder.getKeys().get("id"));
	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_GROUPS_STUDENTS_QUERY, id);
		jdbcTemplate.update(DELETE_GROUP_QUERY, id);
	}

	public Group getById(int id) {
		Group group = jdbcTemplate.queryForObject(GET_BY_ID_REQUEST, (resultSet, rowNum) -> {
			Group newGroup = new Group(id, resultSet.getString("group_name"));
			return newGroup;
		}, id);
		List<Integer> studentsId = jdbcTemplate.query(GET_STUDENTS_BY_GROUP_ID_REQUEST, (resultSet, rowNum) -> {
			return resultSet.getInt("student_id");
		}, id);
		StudentDao studentDao = new StudentDao(jdbcTemplate);
		List<Student> students = new ArrayList<>();
		studentsId.stream().forEach(sId -> {
			students.add(studentDao.getById(sId));
		});

		group.setStudents(students);
		return group;
	}
}
