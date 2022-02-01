package ru.tsar.university.service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.LessonTimeDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.exceptions.InvalidTimeIntervalException;
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

	public Page<LessonTime> getAll(Pageable pageable) {
		return lessonTimeDao.getAll(pageable);
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

	public void verifyLessonTimeExistence(int id) {
		if (lessonTimeDao.getById(id) == null) {
			throw new EntityNotFoundException("LessonTime with id = " + id + " does not exist");
		}
	}

	public void verifyTimeCorrect(LessonTime lessonTime) {
		if (lessonTime.getStartTime().isAfter(lessonTime.getEndTime())) {
			throw new InvalidTimeIntervalException("This time " + lessonTime + " has not correct format");
		}
	}
}
