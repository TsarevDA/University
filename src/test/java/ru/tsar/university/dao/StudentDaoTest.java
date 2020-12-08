package ru.tsar.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import ru.tsar.university.TablesCreation;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;

class StudentDaoTest {

	final static private String GET_STUDENT_REQUEST = "SELECT s.* FROM students s";
	final static private String GET_COUNT_BY_ID_REQUEST = "select count(*) FROM students WHERE id=?";
	final static private String CREATE_STUDENT_QUERY = "INSERT INTO students(first_name, last_name, gender, birth_date, email, phone, address) VALUES(?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private StudentDao studentDao;

	@BeforeEach
	void setUp() throws IOException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringTestConfig.class);
		studentDao = context.getBean("studentDao", StudentDao.class);
		jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
		TablesCreation tablesCreation = context.getBean("tablesCreation", TablesCreation.class);
		tablesCreation.createTables();
	}

	@Test
	void setStudent_whenCreate_thenCreateStudent() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = new Student("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		studentDao.create(expected);
		List<Student> students = jdbcTemplate.query(GET_STUDENT_REQUEST, (resultSet, rowNum) -> {
			Student newStudent = new Student(resultSet.getInt("id"), resultSet.getString("first_name"),
					resultSet.getString("last_name"), Gender.valueOf(resultSet.getString("gender")),
					resultSet.getDate("birth_date").toLocalDate(), resultSet.getString("email"),
					resultSet.getString("phone"), resultSet.getString("address"));
			return newStudent;
		});
		Student actual = students.get(students.size() - 1);
		assertEquals(expected, actual);
	}

	@Test
	void setId_whenDeleteById_thenDeleteStudent() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student student = new Student("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		studentDao.create(student);
		studentDao.deleteById(student.getId());

		int actual = jdbcTemplate.queryForObject(GET_COUNT_BY_ID_REQUEST, Integer.class, student.getId());
		assertEquals(0, actual);
	}

	@Test
	void setId_whenGetById_thenGetStudent() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Student expected = new Student("Ivan", "Ivanov", Gender.valueOf("MALE"),
				LocalDate.parse("1990-01-01", formatter), "mail@mail.ru", "88008080", "Ivanov street, 25-5");
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_STUDENT_QUERY, Statement.RETURN_GENERATED_KEYS);
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
		Student actual = studentDao.getById(expected.getId());
		assertEquals(expected, actual);
	}

}
