package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.exceptions.StudentExistException;
import ru.tsar.university.exceptions.TeacherExistException;
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
		return teacherDao.getById(id);
	}

	public List<Teacher> getAll() {
		return teacherDao.getAll();
	}

	public void update(Teacher teacher) throws TeacherExistException {
		if (isTeacherExist(teacher.getId())) {
			teacherDao.update(teacher);
		} else {
			throw new TeacherExistException(teacher);
		}
	}

	public void deleteById(int id) throws TeacherExistException {
		if (isTeacherExist(id)) {
			teacherDao.deleteById(id);
		} else {
			throw new TeacherExistException(id);
		}
	}

	public boolean isTeacherExist(int id) {
		return teacherDao.getById(id) != null;
	}
}
