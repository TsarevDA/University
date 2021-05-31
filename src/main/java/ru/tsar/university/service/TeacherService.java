package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.exceptions.StudentNotExistException;
import ru.tsar.university.exceptions.TeacherNotExistException;
import ru.tsar.university.model.Teacher;

@Service
public class TeacherService {

	private static final Logger LOG = LoggerFactory.getLogger(TeacherService.class);
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

	public void update(Teacher teacher) {
		try {
			isTeacherExist(teacher.getId());
			teacherDao.update(teacher);
		} catch (TeacherNotExistException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void deleteById(int id)  {
		try {
			isTeacherExist(id);
			teacherDao.deleteById(id);
		} catch (TeacherNotExistException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void isTeacherExist(int id) throws TeacherNotExistException {
		if (teacherDao.getById(id) == null) {
			throw new TeacherNotExistException(id);
		}
	}
}
