package ru.tsar.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import ru.tsar.university.mapper.LessonRowMapper;

import ru.tsar.university.model.Lesson;

@Component
public class LessonDao {

	final static private String CREATE_LESSON_QUERY = "INSERT INTO lessons(course_id,teacher_id,day,lesson_time,auditorium) VALUES(?,?,?,?,?)";
	final static private String CREATE_LESSONS_GROUPS_QUERY = "INSERT INTO lessons_groups(lesson_id,group_id) VALUES(?,?)";
	final static private String DELETE_LESSONS_GROUPS_QUERY = "DELETE FROM lessons_groups where lesson_id =?";
	final static private String DELETE_LESSON_QUERY = "DELETE FROM lessons where id =?";
	final static private String GET_BY_ID_QUERY = "SELECT l.id,l.day,"
			+ "c.id as course_id,c.name as course_name, c.description, "
			+ "t.id as teacher_id, t.first_name, t.last_name, t.gender, t.birth_date, t.email, t.phone, t.address, "
			+ "lt.id as lesson_time_id, lt.order_number, lt.start_time, lt.end_time, "
			+ "a.id as auditorium_id, a.name as auditorium_name,a.capacity "
			+ "FROM lessons l left join courses c on l.course_id=c.id " + "left join teachers t on l.teacher_id=t.id "
			+ "left join lessons_time lt on lt.order_number = l.lesson_time "
			+ "left join auditoriums a on l.auditorium= a.id WHERE l.id =?";


	private JdbcTemplate jdbcTemplate;

	public LessonDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void create(Lesson lesson) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_LESSON_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, lesson.getCourse().getId());
			ps.setInt(2, lesson.getTeacher().getId());
			ps.setDate(3, java.sql.Date.valueOf(lesson.getDay()));
			ps.setInt(4, lesson.getTime().getOrderNumber());
			ps.setInt(5, lesson.getAuditorium().getId());
			return ps;
		}, holder);
		lesson.setId((int) holder.getKeys().get("id"));

		lesson.getGroup().stream()
				.forEach(g -> jdbcTemplate.update(CREATE_LESSONS_GROUPS_QUERY, lesson.getId(), g.getId()));
	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_LESSONS_GROUPS_QUERY, id);
		jdbcTemplate.update(DELETE_LESSON_QUERY, id);
	}

	public Lesson getById(int id) {

		LessonRowMapper rowMapper = new LessonRowMapper(jdbcTemplate);
		Lesson lesson = jdbcTemplate.queryForObject(GET_BY_ID_QUERY, rowMapper, id);

		return lesson;
	}

}
