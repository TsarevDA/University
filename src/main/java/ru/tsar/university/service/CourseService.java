package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.exceptions.CourseNotExistException;
import ru.tsar.university.exceptions.NotUniqueNameException;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Teacher;

@Service
public class CourseService {

	private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);
	private CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void create(Course course) {
		try {
			isUniqueName(course);
			courseDao.create(course);
		} catch (NotUniqueNameException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void update(Course course) {
		try {
			isCourseExist(course.getId());
			isUniqueName(course);
			courseDao.update(course);
		} catch (CourseNotExistException | NotUniqueNameException e) {
			LOG.warn(e.getMessage());
		}
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
		try {
			isCourseExist(id);
			courseDao.deleteById(id);
		} catch (CourseNotExistException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void isCourseExist(int id) throws CourseNotExistException {
		if (courseDao.getById(id) == null) {
			throw new CourseNotExistException(id);
		}
	}

	public void isUniqueName(Course course) throws NotUniqueNameException {
		Course courseByName = courseDao.getByName(course);
		if (courseByName != null) {
			if (courseByName.getId() != course.getId()) {
				throw new NotUniqueNameException(course.getName());
			}
		}
	}

}
