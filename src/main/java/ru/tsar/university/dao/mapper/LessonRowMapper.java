package ru.tsar.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

	private TeacherDao teacherDao;
	private LessonTimeDao lessonTimeDao;
	private CourseDao courseDao;
	private AuditoriumDao auditoriumDao;
	private GroupDao groupDao;

	public LessonRowMapper(TeacherDao teacherDao, LessonTimeDao lessonTimeDao, CourseDao courseDao,
			AuditoriumDao auditoriumDao, GroupDao groupDao) {
		this.teacherDao = teacherDao;
		this.lessonTimeDao = lessonTimeDao;
		this.courseDao = courseDao;
		this.auditoriumDao = auditoriumDao;
		this.groupDao = groupDao;
	}

	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {

		Teacher teacher = teacherDao.getById(rs.getInt("teacher_id"));
		LessonTime lessonTime = lessonTimeDao.getById(rs.getInt("lesson_time_id"));
		Course course = courseDao.getById(rs.getInt("course_id"));
		Auditorium auditorium = auditoriumDao.getById(rs.getInt("auditorium_id"));
		List<Group> groups = groupDao.getByLessonId(rs.getInt("id"));

		return Lesson.builder().
				id(rs.getInt("id")).
				course(course).teacher(teacher)
				.day(rs.getDate("day").toLocalDate()).
				time(lessonTime).
				auditorium(auditorium).
				group(groups).
				build();
	}
}