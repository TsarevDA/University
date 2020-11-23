package ru.tsar.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import ru.tsar.university.model.Group;

public class GroupDao {

	final private String ADD_GROUP_QUERY = "INSERT INTO groups(group_name) VALUES(?)";
	final private String ADD_GROUPS_STUDENTS_QUERY = "INSERT INTO groups_students(group_id,student_id) VALUES(?,?)";
	final private String DELETE_GROUPS_STUDENTS_QUERY = "DELETE FROM groups_students where group_id =?";
	final private String DELETE_GROUP_QUERY = "DELETE FROM groups where group_id =?";
	private JdbcTemplate jdbcTemplate;

	public GroupDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void addGroup(Group group) {
		jdbcTemplate.update(ADD_GROUP_QUERY, group.getName());
		// group.getStudents().stream()
		// .forEach(s -> jdbcTemplate.update(ADD_GROUPS_STUDENTS_QUERY, group.getId(),
		// s.getId()));
	}

	public void deleteGroup(int id) {
		jdbcTemplate.update(DELETE_GROUPS_STUDENTS_QUERY, id);
		jdbcTemplate.update(DELETE_GROUP_QUERY, id);
	}
}
