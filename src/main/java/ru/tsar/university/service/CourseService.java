package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.exceptions.CourseExistException;
import ru.tsar.university.exceptions.UniqueNameException;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Teacher;

@Service
public class CourseService {
	
	private CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void create(Course course) throws UniqueNameException {
		if (isUniqueName(course)) {
			courseDao.create(course);
		} else {
			throw new UniqueNameException(course.getName());
		}
	}

	public void update(Course course) throws UniqueNameException, CourseExistException {
		if (isCourseExist(course.getId())) {
			if (isUniqueName(course)) {
			courseDao.update(course);
			} else {
				throw new UniqueNameException(course.getName());
			}
		} else {
			throw new CourseExistException(course);
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

	public void deleteById(int id) throws CourseExistException {
		if (isCourseExist(id)) {
			courseDao.deleteById(id);
		} else {
			throw new CourseExistException(id);
		}
	}

	public boolean isCourseExist(int id) {
		return courseDao.getById(id) != null;
	}

	public boolean isUniqueName(Course course) {
		Course courseByName = courseDao.getByName(course);
		return (courseByName == null || courseByName.getId() == course.getId());
	}


	
}
