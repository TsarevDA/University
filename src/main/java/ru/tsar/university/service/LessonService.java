package ru.tsar.university.service;

import org.springframework.stereotype.Component;

import ru.tsar.university.dao.LessonDao;
import ru.tsar.university.model.Lesson;

@Component
public class LessonService {
	
	private LessonDao lessonDao;
	
	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}
	
	public void create(Lesson lesson) {
		lessonDao.create(lesson);
	}
	
	public void getById(Lesson lesson) {
		lessonDao.getById(lesson.getId());
	}
	
	public void getAll() {
		lessonDao.getAll();
	}
	
	public void update(Lesson lesson) {
		lessonDao.update(lesson);
	}
	
	public void deleteById(Lesson lesson) {
		lessonDao.deleteById(lesson.getId());
	}
}
