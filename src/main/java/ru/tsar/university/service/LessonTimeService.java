package ru.tsar.university.service;

import org.springframework.stereotype.Component;

import ru.tsar.university.dao.LessonTimeDao;
import ru.tsar.university.model.LessonTime;

@Component
public class LessonTimeService {
	
	private LessonTimeDao lessonTimeDao;
	
	public LessonTimeService (LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}
	
	public void create(LessonTime lessonTime) {
		lessonTimeDao.create(lessonTime);
	}
	
	public void getById(int id) {
		lessonTimeDao.getById(id);
	}
	
	public void getAll() {
		lessonTimeDao.getAll();
	}
	
	public void update(LessonTime lessonTime) {
		lessonTimeDao.update(lessonTime);
	}
	
	public void deleteById(LessonTime lessonTime) {
		lessonTimeDao.deleteById(lessonTime.getId());
	}
}
