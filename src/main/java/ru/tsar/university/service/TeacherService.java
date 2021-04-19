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
		Teacher teacher = teacherDao.getById(id);
		if (teacher != null) {
			return teacher;
		} else {
			return null;
		}
	}

	public List<Teacher> getAll() {
		return teacherDao.getAll();
	}

	public void update(Teacher teacher) {
		if (isExistId(teacher.getId())) {
			teacherDao.update(teacher);
		}
	}

	public void deleteById(int id) {
		if (isExistId(id)) {
			teacherDao.deleteById(id);
		}
	}

	public boolean isExistId(int id) {
		return teacherDao.getById(id) != null;
	}
}
