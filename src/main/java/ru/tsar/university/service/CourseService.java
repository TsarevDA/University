package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Teacher;

@Service
public class CourseService {

	private CourseDao courseDao;
	private final Logger log = LoggerFactory.getLogger(CourseService.class);

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void create(Course course) {
		if (isUniqueName(course)) {
			courseDao.create(course);
		} else {
			log.warn("Create error, name {} is not unique", course.getName());
		}
	}

	public void update(Course course) {
		if (isCourseExist(course.getId())) {
			if (isUniqueName(course)) {
			courseDao.update(course);
			} else {
				log.warn("Update error, name {} is not unique", course.getName());
			}
		} else {
			log.warn("Update error, course {} is already exist", course);
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
		if (isCourseExist(id)) {
			courseDao.deleteById(id);
		} else {
			log.warn("deteleById error, id = {} is not exist", id);
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
