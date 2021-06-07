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

	private LessonTimeDao lessonTimeDao;

	public LessonTimeService(LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}

	public void create(LessonTime lessonTime) {
		verifyTimeCorrect(lessonTime);
		lessonTimeDao.create(lessonTime);
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
		verifyLessonTimeExistence(lessonTime.getId());
		verifyTimeCorrect(lessonTime);
		lessonTimeDao.update(lessonTime);
	}

	public void deleteById(int id) {
		verifyLessonTimeExistence(id);
		lessonTimeDao.deleteById(id);
	}

	public void verifyLessonTimeExistence(int id) throws LessonTimeNotExistException {
		if (lessonTimeDao.getById(id) == null) {
			throw new LessonTimeNotExistException("LessonTime with id = " + id + " does not exist");
		}
	}

	public void verifyTimeCorrect(LessonTime lessonTime) throws TimeNotCorrectException {
		if (lessonTime.getStartTime().isAfter(lessonTime.getEndTime())) {
			throw new TimeNotCorrectException("This time " + lessonTime + " has not correct format");
		}
	}
}
