package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.mapper.GroupRowMapper;
import ru.tsar.university.model.Group;


@Component
public class GroupDao {

	final static private String ADD_GROUP_QUERY = "INSERT INTO groups(name) VALUES(?)";
	final static private String DELETE_GROUP_QUERY = "DELETE FROM groups where id =?";
	final static private String GET_BY_ID_QUERY = "SELECT * FROM groups WHERE id=?";
	final static private String GET_STUDENTS_GROUPS_COUNT_QUERY = "SELECT count(student_id) FROM groups_students WHERE group_id=?";
	final static private String GET_GROUPS_BY_LESSOM_QUERY = "SELECT g.* FROM lessons_groups lg left join groups g on lg.group_id = g.id WHERE group_id = ?";
	
	private JdbcTemplate jdbcTemplate;
	

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
		Integer studentCount = jdbcTemplate.queryForObject(GET_STUDENTS_GROUPS_COUNT_QUERY, Integer.class, id);
		
		if (studentCount==0) {
			jdbcTemplate.update(DELETE_GROUP_QUERY, id);	
		} 
		
	}

	public Group getById(int id) {
		GroupRowMapper rowMapper = new GroupRowMapper();
		Group group =  jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);

	StudentDao studentDao = new StudentDao(jdbcTemplate);
	group.setStudents(studentDao.getStudentsByGroup(group));
	return group;

	}
	
	public List<Group> getGroupsByLessonId(int id) {
		GroupRowMapper rowMapper = new GroupRowMapper();
		return jdbcTemplate.query(GET_GROUPS_BY_LESSOM_QUERY, rowMapper, id);
	}
	
}
