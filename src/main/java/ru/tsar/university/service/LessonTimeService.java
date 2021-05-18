package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.LessonTimeDao;
import ru.tsar.university.model.LessonTime;

@Service
public class LessonTimeService {

	private LessonTimeDao lessonTimeDao;
	private final Logger log = LoggerFactory.getLogger(LessonTimeService.class);

	public LessonTimeService(LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}

	public void create(LessonTime lessonTime) {
		if (isTimeCorrect(lessonTime)) {
			lessonTimeDao.create(lessonTime);
		} else {
			log.warn("Create error, lessonTime {} is not correct", lessonTime);
		}
	}

	public LessonTime getById(int id) {
		return lessonTimeDao.getById(id);
	}

	public LessonTime getByOrder(int order) {
		return lessonTimeDao.getByOrder(order);
	}

	public List<LessonTime> getAll() {
		return lessonTimeDao.getAll();
	}

	public void update(LessonTime lessonTime) {
		if (isLessonTimeExist(lessonTime.getId()) && isTimeCorrect(lessonTime)) {
			if (isTimeCorrect(lessonTime)) {
			lessonTimeDao.update(lessonTime);
			} else {
				log.warn("Update error, LessonTime {} is not correct", lessonTime);
			}
		} else {
			log.warn("LessonTime, id = {} is not exist", lessonTime.getId());
		}

	}

	public void deleteById(int id) {
		if (isLessonTimeExist(id)) {
			lessonTimeDao.deleteById(id);
		} else {
			log.warn("LessonTime, id = {} is not exist", id);
		}
	}

	public boolean isLessonTimeExist(int id) {
		return lessonTimeDao.getById(id) != null;
	}

	public boolean isTimeCorrect(LessonTime lessonTime) {
		return lessonTime.getStartTime().isBefore(lessonTime.getEndTime());
	}
}
