package ru.tsar.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.LessonTimeDao;
import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Teacher;

@Component
public class LessonRowMapper implements RowMapper<Lesson> {

	@Autowired
	private TeacherDao teacherDao;
	@Autowired
	private LessonTimeDao lessonTimeDao;
	@Autowired
	private CourseDao courseDao;
	@Autowired
	private AuditoriumDao auditoriumDao;
	@Autowired
	private GroupDao groupDao;

	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {

		Teacher teacher = teacherDao.getById(rs.getInt("teacher_id"));
		LessonTime lessonTime = lessonTimeDao.getByOrder(rs.getInt("order_number"));
		Course course = courseDao.getById(rs.getInt("course_id"));
		Auditorium auditorium = auditoriumDao.getById(rs.getInt("auditorium_id"));
		List<Group> groups = groupDao.getByLessonId(rs.getInt("id"));

		return new Lesson(rs.getInt("id"), course, teacher, groups, rs.getDate("day").toLocalDate(), lessonTime,
				auditorium);
	}
}