package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.dao.mapper.GroupRowMapper;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;

@Component
public class GroupDao {

	private static final String ADD_GROUP_QUERY = "INSERT INTO groups(name) VALUES(?)";
	private static final String DELETE_GROUP_QUERY = "DELETE FROM groups where id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM groups WHERE id=?";
	private static final String GET_STUDENTS_GROUPS_COUNT_QUERY = "SELECT count(student_id) FROM groups_students WHERE group_id=?";
	private static final String GET_GROUPS_BY_LESSOM_QUERY = "SELECT g.* FROM lessons_groups lg left join groups g on lg.group_id = g.id WHERE group_id = ?";
	private static final String UPDATE_GROUP_QUERY = "UPDATE groups SET name=? WHERE id=?";
	private static final String GET_ALL_QUERY = "SELECT * FROM groups ";
	private static final String EXIST_NAME_QUERY = "SELECT count(*) FROM groups WHERE name = ?";
	private static final String GET_BY_DAY_TIME_GROUP_QUERY = "SELECT count(*) FROM lessons l left join lessons_groups lg on l.id = lg.lesson_id WHERE day=? AND lesson_time_id = ? AND group_id =?";
	private static final String EXIST_ID_QUERY = "SELECT count(*) FROM groups WHERE id=?";
	
	private JdbcTemplate jdbcTemplate;
	private GroupRowMapper rowMapper;
	private StudentDao studentDao;

	public GroupDao(JdbcTemplate jdbcTemplate, GroupRowMapper rowMapper, StudentDao studentDao) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
		this.studentDao = studentDao;
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

		if (studentCount == 0) {
			jdbcTemplate.update(DELETE_GROUP_QUERY, id);
		}

	}

	public Group getById(int id) {
		Group group = jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
		group.setStudents(studentDao.getByGroup(group));
		return group;

	}

	public List<Group> getByLessonId(int id) {
		return jdbcTemplate.query(GET_GROUPS_BY_LESSOM_QUERY, rowMapper, id);
	}

	public void update(Group group) {
		jdbcTemplate.update(UPDATE_GROUP_QUERY, group.getName(), group.getId());
	}

	public List<Group> getAll() {
		return jdbcTemplate.query(GET_ALL_QUERY, rowMapper);
	}
	
	public boolean checkNameExist(Group group) {
		int count = jdbcTemplate.queryForObject(EXIST_NAME_QUERY, Integer.class, group.getName());
		return (count==0) ? false: true;
	}
	
	public boolean checkGroupFree(Lesson lesson) {
		List<Group> groups = lesson.getGroups();
		for (Group group : groups) {
			int count = jdbcTemplate.queryForObject(GET_BY_DAY_TIME_GROUP_QUERY, Integer.class, lesson.getDay(),
					lesson.getTime().getId(), group.getId());
			if (count != 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkIdExist(int id) {
		int count = jdbcTemplate.queryForObject(EXIST_ID_QUERY, Integer.class, id);
		return (count==0) ? false: true;
	}
}