package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.model.Teacher;

@Service
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	public void create(Teacher teacher) {
		teacherDao.create(teacher);
	}

	public Teacher getById(int id) {
		if (teacherDao.getById(id) !=null) {
		return teacherDao.getById(id);
		} else {
			return null;
		}
	}

	public List<Teacher> getAll() {
		return teacherDao.getAll();
	}

	public void update(Teacher teacher) {
		if (teacherDao.getById(teacher.getId()) !=null ) {
			teacherDao.update(teacher);
		}
	}

	public void deleteById(int id) {
		if (teacherDao.getById(id) !=null) {
			teacherDao.deleteById(id);
		}
	}
}
