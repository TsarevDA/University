package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Teacher;

@Service
public class CourseService {

	private CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void create(Course course) {
		if (!courseDao.checkNameExist(course)) {
			courseDao.create(course);
		}
	}

	public void update(Course course) {
		if (courseDao.checkIdExist(course.getId())) {
			courseDao.update(course);
		}
	}

	public Course getById(int id) {
		if (courseDao.checkIdExist(id)) {
		return courseDao.getById(id); 
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
		if (courseDao.checkIdExist(id)) {
			courseDao.deleteById(id);
		}
	}

}
