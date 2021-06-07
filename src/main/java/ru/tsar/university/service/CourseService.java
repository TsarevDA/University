package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.exceptions.CourseNotExistException;
import ru.tsar.university.exceptions.NotUniqueNameException;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Teacher;

@Service
public class CourseService {

	private CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void create(Course course) {
		verifyNameUniqueness(course);
		courseDao.create(course);
	}

	public void update(Course course) {
		verifyCourseExistence(course.getId());
		verifyNameUniqueness(course);
		courseDao.update(course);
	}

	public Course getById(int id) {
		return courseDao.getById(id);
	}

	public List<Course> getAll() {
		return courseDao.getAll();
	}

	public List<Course> getByTeacherId(Teacher teacher) {
		return courseDao.getByTeacherId(teacher.getId());
	}

	public void deleteById(int id) {
		verifyCourseExistence(id);
		courseDao.deleteById(id);
	}

	public void verifyCourseExistence(int id) throws CourseNotExistException {
		if (courseDao.getById(id) == null) {
			throw new CourseNotExistException("Course with id = " + id + " does not exist");
		}
	}

	public void verifyNameUniqueness(Course course) throws NotUniqueNameException {
		Course courseByName = courseDao.getByName(course);
		if (courseByName != null && courseByName.getId() != course.getId()) {
			throw new NotUniqueNameException("This name is not unique: " + course.getName());
		}
	}

}
