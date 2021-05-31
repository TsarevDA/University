package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.LessonTimeDao;
import ru.tsar.university.exceptions.LessonTimeNotExistException;
import ru.tsar.university.exceptions.TimeNotCorrectException;
import ru.tsar.university.model.LessonTime;

@Service
public class LessonTimeService {

	private static final Logger LOG = LoggerFactory.getLogger(LessonTimeService.class);
	private LessonTimeDao lessonTimeDao;

	public LessonTimeService(LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}

	public void create(LessonTime lessonTime) {
		try {
			isTimeCorrect(lessonTime);
			lessonTimeDao.create(lessonTime);
		} catch (TimeNotCorrectException e) {
			LOG.warn(e.getMessage());
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
		try {
			isLessonTimeExist(lessonTime.getId());
			isTimeCorrect(lessonTime);
			lessonTimeDao.update(lessonTime);
		} catch (LessonTimeNotExistException | TimeNotCorrectException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void deleteById(int id) {
		try {
			isLessonTimeExist(id);
			lessonTimeDao.deleteById(id);
		} catch (LessonTimeNotExistException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void isLessonTimeExist(int id) throws LessonTimeNotExistException {
		if (lessonTimeDao.getById(id) == null) {
			throw new LessonTimeNotExistException(id);
		}
	}

	public void isTimeCorrect(LessonTime lessonTime) throws TimeNotCorrectException {
		if (lessonTime.getStartTime().isAfter(lessonTime.getEndTime())) {
			throw new TimeNotCorrectException(lessonTime);
		}
	}
}
