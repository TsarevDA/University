package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.LessonTimeDao;
import ru.tsar.university.model.LessonTime;

@Service
public class LessonTimeService {

	private LessonTimeDao lessonTimeDao;

	public LessonTimeService(LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}

	public void create(LessonTime lessonTime) {
		if ( lessonTime.getStartTime().isBefore(lessonTime.getEndTime()) ) {
		lessonTimeDao.create(lessonTime);
		}
	}

	public LessonTime getById(int id) {
		if (lessonTimeDao.getById(id) != null) {
		return lessonTimeDao.getById(id);
		} else {
			return null;
		}
	}

	public LessonTime getByOrder(int order) {
		return lessonTimeDao.getByOrder(order);
	}
	
	public List<LessonTime> getAll() {
		return lessonTimeDao.getAll();
	}

	public void update(LessonTime lessonTime) {
		if (lessonTimeDao.getById(lessonTime.getId()) != null && lessonTime.getStartTime().isBefore(lessonTime.getEndTime()) ) {
		lessonTimeDao.update(lessonTime);
		}
		
	}

	public void deleteById(int id) {
		if (lessonTimeDao.getById(id) != null) {
		lessonTimeDao.deleteById(id);
		}
	}
}
