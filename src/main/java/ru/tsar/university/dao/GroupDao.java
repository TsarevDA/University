package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.dao.mapper.GroupRowMapper;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;


@Component
public class GroupDao {

	private static final Logger LOG = LoggerFactory.getLogger(GroupDao.class);
	
	private static final String ADD_GROUP_QUERY = "INSERT INTO groups(name) VALUES(?)";
	private static final String ADD_GROUPS_STUDENTS_QUERY = "INSERT INTO groups_students(group_id,student_id) VALUES(?,?)";
	private static final String DELETE_GROUPS_STUDENTS_QUERY = "DELETE FROM groups_students where group_id = ?";
	private static final String DELETE_GROUP_QUERY = "DELETE FROM groups where id =?";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM groups WHERE id=?";
	private static final String GET_STUDENTS_GROUPS_COUNT_QUERY = "SELECT count(student_id) FROM groups_students WHERE group_id=?";
	private static final String GET_GROUPS_BY_LESSON_QUERY = "SELECT g.* FROM lessons_groups lg left join groups g on lg.group_id = g.id WHERE lesson_id = ?";
	private static final String UPDATE_GROUP_QUERY = "UPDATE groups SET name=? WHERE id=?";
	private static final String GET_ALL_PAGES_QUERY = "SELECT * FROM groups LIMIT ? OFFSET ?";
	private static final String GET_ALL_QUERY = "SELECT * FROM groups";
	private static final String GET_BY_NAME_QUERY = "SELECT * FROM groups WHERE name = ?";
	private static final String GET_COUNT_GROUPS_QUERY = "SELECT count(id) FROM groups";

	private JdbcTemplate jdbcTemplate;
	private GroupRowMapper rowMapper;
	private StudentDao studentDao;

	public GroupDao(JdbcTemplate jdbcTemplate, GroupRowMapper rowMapper, StudentDao studentDao) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
		this.studentDao = studentDao;
	}

	public void create(Group group) {
		LOG.debug("Created group: {}", group);
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(ADD_GROUP_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, group.getName());
			return ps;
		}, holder);
		group.setId((int) holder.getKeys().get("id"));
		group.getStudents().stream()
		.forEach(c -> jdbcTemplate.update(ADD_GROUPS_STUDENTS_QUERY, group.getId(), c.getId()));
	}

	public void deleteById(int id) {
		Integer studentCount = jdbcTemplate.queryForObject(GET_STUDENTS_GROUPS_COUNT_QUERY, Integer.class, id);
		
		if (studentCount == 0) {
			LOG.debug("Deleted group, id = {}", id);
			jdbcTemplate.update(DELETE_GROUP_QUERY, id);
		} else {
			LOG.warn("Group, id = {} have {} students", id, studentCount);
		}

	}

	public Group getById(int id) {
		try {
			Group group = jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
			return group;
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("Group not found by id = {}",id);
			return null;
		}
	}

	public List<Group> getByLessonId(int id) {
		return jdbcTemplate.query(GET_GROUPS_BY_LESSON_QUERY, rowMapper, id);
	}

	public void update(Group group) {
		LOG.debug("Updated to group: {}", group);
		jdbcTemplate.update(UPDATE_GROUP_QUERY, group.getName(), group.getId());
		jdbcTemplate.update(DELETE_GROUPS_STUDENTS_QUERY, group.getId());
		group.getStudents().stream()
		.forEach(c -> jdbcTemplate.update(ADD_GROUPS_STUDENTS_QUERY, group.getId(), c.getId()));
	}

	public Page<Group> getAll(Pageable pageable) {
		int total = jdbcTemplate.queryForObject(GET_COUNT_GROUPS_QUERY, Integer.class);
		List<Group> groups = jdbcTemplate.query(GET_ALL_PAGES_QUERY, rowMapper, pageable.getPageSize() ,pageable.getOffset());
		return new PageImpl<>(groups, pageable, total);
	}
	
	public List<Group> getAll() {
		return jdbcTemplate.query(GET_ALL_QUERY, rowMapper);
	}

	public Group getByName(Group group) {
		try {
			return jdbcTemplate.queryForObject(GET_BY_NAME_QUERY, rowMapper, group.getName());
		} catch (EmptyResultDataAccessException e) {
			LOG.warn("Group not found by name = {}", group.getName());
			return null;
		}
	}
}