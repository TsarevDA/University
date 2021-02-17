package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.jdbc.JdbcTestUtils;

import ru.tsar.university.SpringTestConfig;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;

class GroupDaoTest {

	final static private String GET_GROUP_QUERY = "SELECT * FROM groups";
	final static private String GET_COUNT_BY_ID_QUERY = "select count(*) FROM groups WHERE id=?";
	final static private String CREATE_GROUP_QUERY = "INSERT INTO groups(name) VALUES(?)";

	final static private String CREATE_STUDENTS_GROUPS_QUERY = "INSERT INTO groups_students(group_id,student_id) VALUES(?,?)";

	private AnnotationConfigApplicationContext context;
	private JdbcTemplate jdbcTemplate;
	private GroupDao groupDao;
	private StudentDao studentDao;

	@BeforeEach
	void setUp() {
		context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		context.getBean("databasePopulator", DatabasePopulator.class);
		this.jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		this.groupDao = context.getBean("groupDao", GroupDao.class);
		this.studentDao = context.getBean("studentDao", StudentDao.class);
	}

	@AfterEach
	void setDown() {
		context.close();
	}

	@Test
	void setGroup_whenCreate_thenCreateGroup() {
		Group expected = new Group("T7-09");
		groupDao.create(expected);
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = " + expected.getId());
		assertEquals(1, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteGroup() {
		Group group = new Group("T7-09");
		groupDao.create(group);
		groupDao.deleteById(group.getId());
		int actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "id = " + group.getId());
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
		groupDao.create(expected);
		Group actual = groupDao.getById(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void setGroups_whenGetAll_thenGroupsList() {
		Group first = new Group("T7-09");
		groupDao.create(first);

		Group second = new Group("A7-98");
		groupDao.create(second);

		List<Group> expected = new ArrayList<>();
		expected.add(first);
		expected.add(second);

		List<Group> actual = groupDao.getAll();
		assertEquals(expected, actual);
	}
}
