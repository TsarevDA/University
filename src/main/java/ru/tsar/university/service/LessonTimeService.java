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
		if (isTimeCorrect(lessonTime)) {
			lessonTimeDao.create(lessonTime);
		}
	}

	public LessonTime getById(int id) {
		LessonTime lessonTime = lessonTimeDao.getById(id);
		if (lessonTime != null) {
			return lessonTime;
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
		if (isExistId(lessonTime.getId()) && isTimeCorrect(lessonTime)) {
			lessonTimeDao.update(lessonTime);
		}

	}

	public void deleteById(int id) {
		if (isExistId(id)) {
			lessonTimeDao.deleteById(id);
		}
	}

	public boolean isExistId(int id) {
		return lessonTimeDao.getById(id) != null;
	}

	public boolean isTimeCorrect(LessonTime lessonTime) {
		return lessonTime.getStartTime().isBefore(lessonTime.getEndTime());
	}
}
