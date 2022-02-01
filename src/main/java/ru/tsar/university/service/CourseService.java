package ru.tsar.university.service;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
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

	public Page<Course> getAll(Pageable pageable) {
		return courseDao.getAll(pageable);
	}

	public List<Course> getAll() {
		return courseDao.getAll();
	}
	
	public List<Course> getByTeacherId(int id) {
		return courseDao.getByTeacherId(id);
	}
	
	public Page<Course> getByTeacherId(int id, Pageable pageable) {
		return courseDao.getByTeacherId(id, pageable);
	}

	public void deleteById(int id) {
		verifyCourseExistence(id);
		courseDao.deleteById(id);
	}

	public void verifyCourseExistence(int id) {
		if (courseDao.getById(id) == null) {
			throw new EntityNotFoundException("Course with id = " + id + " does not exist");
		}
	}

	public void verifyNameUniqueness(Course course) {
		Course courseByName = courseDao.getByName(course);
		if (courseByName != null && courseByName.getId() != course.getId()) {
			throw new NotUniqueNameException("This name is not unique: " + course.getName());
		}
	}

}
