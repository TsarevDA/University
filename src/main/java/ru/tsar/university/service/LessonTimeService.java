package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.LessonTimeDao;
import ru.tsar.university.exceptions.GroupExistException;
import ru.tsar.university.exceptions.LessonTimeExistException;
import ru.tsar.university.exceptions.TimeCorrectException;
import ru.tsar.university.model.LessonTime;

@Service
public class LessonTimeService {

	private static final Logger LOG = LoggerFactory.getLogger(LessonTimeService.class);
	private LessonTimeDao lessonTimeDao;

	public LessonTimeService(LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}

	public void create(LessonTime lessonTime) throws TimeCorrectException {
		if (isTimeCorrect(lessonTime)) {
			lessonTimeDao.create(lessonTime);
		} else {
			throw new TimeCorrectException(lessonTime);
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

	public void update(LessonTime lessonTime) throws LessonTimeExistException, TimeCorrectException {
		if (isLessonTimeExist(lessonTime.getId()) && isTimeCorrect(lessonTime)) {
			if (isTimeCorrect(lessonTime)) {
			lessonTimeDao.update(lessonTime);
			} else {
				throw new TimeCorrectException(lessonTime);
			}
		} else {
			throw new LessonTimeExistException(lessonTime);
		}

	}

	public void deleteById(int id) throws LessonTimeExistException {
		if (isLessonTimeExist(id)) {
			lessonTimeDao.deleteById(id);
		} else {
			throw new LessonTimeExistException(id);
		}
	}

	public boolean isLessonTimeExist(int id) {
		return lessonTimeDao.getById(id) != null;
	}

	public boolean isTimeCorrect(LessonTime lessonTime) {
		return lessonTime.getStartTime().isBefore(lessonTime.getEndTime());
	}
}
