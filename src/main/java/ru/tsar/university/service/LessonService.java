package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.LessonDao;
import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.model.Lesson;

@Service
public class LessonService {

	private LessonDao lessonDao;
	private AuditoriumDao auditoriumDao;
	private TeacherDao teacherDao;
	private GroupDao groupDao;

	public LessonService(LessonDao lessonDao, AuditoriumDao auditoriumDao, TeacherDao teacherDao, GroupDao groupDao) {
		this.lessonDao = lessonDao;
		this.auditoriumDao = auditoriumDao;
		this.teacherDao = teacherDao;
		this.groupDao = groupDao;
	}

	public void create(Lesson lesson) {
		if (auditoriumDao.checkAuditoriumFree(lesson) & teacherDao.checkTeacherFree(lesson)
				& groupDao.checkGroupFree(lesson)) {
			lessonDao.create(lesson);
		}
	}

	public Lesson getById(int id) {
		if (lessonDao.checkIdExist(id)) {
		return lessonDao.getById(id);
		} else {
			return null;
		}
	}

	public List<Lesson> getAll() {
		return lessonDao.getAll();
	}

	public void update(Lesson lesson) {
		if (lessonDao.checkIdExist(lesson.getId())) {
		lessonDao.update(lesson);
		}
	}

	public void deleteById(int id) {
		if (lessonDao.checkIdExist(id)) {
		lessonDao.deleteById(id);
		}
	}
}
