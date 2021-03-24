package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;

import ru.tsar.university.dao.CourseDao;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Teacher;

@Component
public class CourseService {
	
	private CourseDao courseDao; 
	
	public CourseService (CourseDao courseDao){
		this.courseDao = courseDao;
	}
	
	public void create(Course course) {
		courseDao.create(course);		
	}
	
	public void update(Course course) {
		courseDao.update(course);
	}
	
	public Course getById(Course course) {
		return courseDao.getById(course.getId());
	}
	
	public List<Course> getByTeacherId(Teacher teacher) {
		return courseDao.getByTeacherId(teacher.getId());
	}
	
	public void deleteById(Course course) {
		courseDao.deleteById(course.getId());
	}

}
