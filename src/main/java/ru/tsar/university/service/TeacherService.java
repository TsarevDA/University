package ru.tsar.university.service;

import org.springframework.stereotype.Component;

import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.model.Teacher;

@Component
public class TeacherService {
	
	private TeacherDao teacherDao;
	
	public TeacherService (TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}
	
	public void create(Teacher teacher) {
		teacherDao.create(teacher);
	}
	
	public void getById(int id) {
		teacherDao.getById(id);
	}
	
	public void getAll() {
		teacherDao.getAll();
	}
	public void update(Teacher teacher) {
		teacherDao.update(teacher);
	}
	
	public void deleteById(Teacher teacher) {
		teacherDao.deleteById(teacher.getId());
	}
}
