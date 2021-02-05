package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ru.tsar.university.SpringConfig;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;

@SpringJUnitConfig(classes = SpringConfig.class)
@Sql("/schema.sql")
class GroupDaoTest {

	final static private String GET_GROUP_QUERY = "SELECT * FROM groups";
	final static private String GET_COUNT_BY_ID_QUERY = "select count(*) FROM groups WHERE id=?";
	final static private String CREATE_GROUP_QUERY = "INSERT INTO groups(name) VALUES(?)";

	final static private String CREATE_STUDENTS_GROUPS_QUERY = "INSERT INTO groups_students(group_id,student_id) VALUES(?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private StudentDao studentDao;

	@Test
	void setGroup_whenCreate_thenCreateGroup() {
		Group expected = new Group("T7-09");
		groupDao.create(expected);
		List<Group> groups = jdbcTemplate.query(GET_GROUP_QUERY, (resultSet, rowNum) -> {
			Group newGroup = new Group(resultSet.getInt("id"), resultSet.getString("name"));
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

		int actual = jdbcTemplate.queryForObject(GET_COUNT_BY_ID_QUERY, Integer.class, group.getId());
		assertEquals(0, actual);
	}

	@Test
	void setGroupWithStudents_whenDeleteById_thenGroupWithStudents() {
		Group group = new Group("T7-09");
		groupDao.create(group);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student student = new Student("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");

		studentDao.create(student);
		jdbcTemplate.update(CREATE_STUDENTS_GROUPS_QUERY, 1, 1);
		groupDao.deleteById(group.getId());

		int actual = jdbcTemplate.queryForObject(GET_COUNT_BY_ID_QUERY, Integer.class, group.getId());
		assertEquals(1, actual);
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
