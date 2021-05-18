package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.dao.mapper.GroupRowMapper;
import ru.tsar.university.model.Group;


@Component
public class GroupDao {

	private static final String ADD_GROUP_QUERY = "INSERT INTO groups(name) VALUES(?)";
	private static final String DELETE_GROUP_QUERY = "DELETE FROM groups where id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM groups WHERE id=?";
	private static final String GET_STUDENTS_GROUPS_COUNT_QUERY = "SELECT count(student_id) FROM groups_students WHERE group_id=?";
	private static final String GET_GROUPS_BY_LESSOM_QUERY = "SELECT g.* FROM lessons_groups lg left join groups g on lg.group_id = g.id WHERE group_id = ?";
	private static final String UPDATE_GROUP_QUERY = "UPDATE groups SET name=? WHERE id=?";
	private static final String GET_ALL_QUERY = "SELECT * FROM groups ";
	private static final String GET_BY_NAME_QUERY = "SELECT * FROM groups WHERE name = ?";

	private JdbcTemplate jdbcTemplate;
	private GroupRowMapper rowMapper;
	private StudentDao studentDao;
	private final Logger log = LoggerFactory.getLogger(GroupDao.class);

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
		log.info("Call create {}", group);
	}

	public void deleteById(int id) {
		Integer studentCount = jdbcTemplate.queryForObject(GET_STUDENTS_GROUPS_COUNT_QUERY, Integer.class, id);
		
		if (studentCount == 0) {
			jdbcTemplate.update(DELETE_GROUP_QUERY, id);
			log.info("Call deleteById id = {}", id);
		} else {
			log.warn("Group, id = {} have {} students", id, studentCount);
		}

	}

	public Group getById(int id) {
		try {
			Group group = jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
			group.setStudents(studentDao.getByGroupId(id));
			return group;
		} catch (EmptyResultDataAccessException e) {
			log.warn("EmptyResultSet, getById: {}",id);
			return null;
		}
	}

	public List<Group> getByLessonId(int id) {
		return jdbcTemplate.query(GET_GROUPS_BY_LESSOM_QUERY, rowMapper, id);
	}

	public void update(Group group) {
		jdbcTemplate.update(UPDATE_GROUP_QUERY, group.getName(), group.getId());
		log.info("Call update {}", group);
	}

	public List<Group> getAll() {
		return jdbcTemplate.query(GET_ALL_QUERY, rowMapper);
	}

	public Group getByName(Group group) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_NAME_QUERY, rowMapper, group.getName());
		} catch (EmptyResultDataAccessException e) {
			log.warn("EmptyResultSet, getByName: {}", group.getName());
			return null;
		}
	}
}