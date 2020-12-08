package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.TablesCreation;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;

@Component
class GroupDaoTest {

	final static private String GET_GROUP_REQUEST = "SELECT g.* FROM groups g";
	final static private String GET_COUNT_BY_ID_REQUEST = "select count(*) FROM groups WHERE id=?";
	final static private String CREATE_GROUP_QUERY = "INSERT INTO groups(group_name) VALUES(?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GroupDao groupDao;

	@BeforeEach
	void setUp() throws IOException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		groupDao = context.getBean("groupDao", GroupDao.class);
		jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		TablesCreation tablesCreation = context.getBean("tablesCreation", TablesCreation.class);
		tablesCreation.createTables();
	}

	@Test
	void setGroup_whenCreate_thenCreateGroup() {
		Group expected = new Group("T7-09");
		groupDao.create(expected);
		List<Group> groups = jdbcTemplate.query(GET_GROUP_REQUEST, (resultSet, rowNum) -> {
			Group newGroup = new Group(resultSet.getInt("id"), resultSet.getString("group_name"));
			return newGroup;
		});
		Group actual = groups.get(groups.size() - 1);
		assertEquals(expected, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteGroup() {
		Group group = new Group("T7-09");
		groupDao.create(group);
		groupDao.deleteById(group.getId());

		int actual = jdbcTemplate.queryForObject(GET_COUNT_BY_ID_REQUEST, Integer.class, group.getId());
		assertEquals(0, actual);
	}

	@Test
	void setId_whenGetById_thenGetGroup() {
		Group expected = new Group("T7-09");
		ArrayList<Student> students = new ArrayList<>();
		expected.setStudents(students);
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_GROUP_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, expected.getName());
			return ps;
		}, holder);
		expected.setId((int) holder.getKeys().get("id"));
		Group actual = groupDao.getById(expected.getId());

		assertEquals(expected, actual);
	}
}
