package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import ru.tsar.university.TablesCreation;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Teacher;

class TeacherDaoTest {

	final static private String GET_TEACHER_REQUEST = "SELECT t.* FROM teachers t";
	final static private String GET_COUNT_BY_ID_REQUEST = "select count(*) FROM teachers WHERE id=?";
	final static private String CREATE_TEACHER_QUERY = "INSERT INTO teachers(first_name, last_name, gender, birth_date, email, phone, address) VALUES(?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private TeacherDao teacherDao;

	@BeforeEach
	void setUp() throws IOException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		teacherDao = context.getBean("teacherDao", TeacherDao.class);
		jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		TablesCreation tablesCreation = context.getBean("tablesCreation", TablesCreation.class);
		tablesCreation.createTables();
	}

	@Test
	void setTeacher_whenCreate_thenCreateTeacher() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher expected = new Teacher("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		teacherDao.create(expected);
		List<Course> courses = new ArrayList<>();
		List<Teacher> teachers = jdbcTemplate.query(GET_TEACHER_REQUEST, (resultSet, rowNum) -> {
			Teacher newTeacher = new Teacher(resultSet.getInt("id"), resultSet.getString("first_name"),
					resultSet.getString("last_name"), Gender.valueOf(resultSet.getString("gender")),
					resultSet.getDate("birth_date").toLocalDate(), resultSet.getString("email"),
					resultSet.getString("phone"), resultSet.getString("address"));
			return newTeacher;
		});
		Teacher actual = teachers.get(teachers.size() - 1);
		actual.setCourses(courses);
		assertEquals(expected, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteTeacher() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Teacher teacher = new Teacher("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		teacherDao.create(teacher);
		teacherDao.deleteById(teacher.getId());

		int actual = jdbcTemplate.queryForObject(GET_COUNT_BY_ID_REQUEST, Integer.class, teacher.getId());
		assertEquals(0, actual);
	}

	@Test
	void setId_whenGetById_thenGetTeacher() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<Course> courses = new ArrayList<>();
		Teacher expected = new Teacher("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		expected.setCourses(courses);
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_TEACHER_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, expected.getFirstName());
			ps.setString(2, expected.getLastName());
			ps.setString(3, expected.getGender().toString());
			ps.setDate(4, java.sql.Date.valueOf(expected.getBirthDate()));
			ps.setString(5, expected.getEmail());
			ps.setString(6, expected.getPhone());
			ps.setString(7, expected.getAddress());
			return ps;
		}, holder);
		expected.setId((int) holder.getKeys().get("id"));
		Teacher actual = teacherDao.getById(expected.getId());
		assertEquals(expected, actual);
	}

}
