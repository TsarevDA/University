package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.model.Teacher;

@Service
public class TeacherService {

	private TeacherDao teacherDao;
	private final Logger log = LoggerFactory.getLogger(TeacherService.class);

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
		if (isTeacherExist(teacher.getId())) {
			teacherDao.update(teacher);
		} else {
			log.warn("Update error, teacher, id = {} is not exist", teacher.getId());
		}
	}

	public void deleteById(int id) {
		if (isTeacherExist(id)) {
			teacherDao.deleteById(id);
		} else {
			log.warn("deleteById error, teacher, id = {} is not exist", id);
		}
	}

	public boolean isTeacherExist(int id) {
		return teacherDao.getById(id) != null;
	}
}
