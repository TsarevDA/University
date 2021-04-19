package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Teacher;

@Service
public class CourseService {

	private CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void create(Course course) {
		course.setId(0);
		if (isUniqueName(course)) {
			courseDao.create(course);
		}
	}

	public void update(Course course) {
		if (isExistId(course.getId()) && isUniqueName(course)) {
			courseDao.update(course);
		}
	}

	public Course getById(int id) {
		Course course = courseDao.getById(id);
		if (course != null) {
			return course;
		} else {
			return null;
		}
	}

	public List<Course> getAll() {
		return courseDao.getAll();
	}

	public List<Course> getByTeacherId(Teacher teacher) {
		return courseDao.getByTeacherId(teacher.getId());
	}

	public void deleteById(int id) {
		if (isExistId(id)) {
			courseDao.deleteById(id);
		}
	}

	public boolean isExistId(int id) {
		return courseDao.getById(id) != null;
	}

	public boolean isUniqueName(Course course) {
		Course courseByName = courseDao.getByName(course);
		return (courseByName == null || courseByName.getId() == course.getId());
	}

}
