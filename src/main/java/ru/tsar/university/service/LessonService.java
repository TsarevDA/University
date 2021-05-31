package ru.tsar.university.service;

import java.time.DayOfWeek;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.LessonDao;
import ru.tsar.university.exceptions.AuditoriumNotFreeException;
import ru.tsar.university.exceptions.CapacityNotEnoughException;
import ru.tsar.university.exceptions.DayOffException;
import ru.tsar.university.exceptions.GroupNotExistException;
import ru.tsar.university.exceptions.GroupNotFreeException;
import ru.tsar.university.exceptions.LessonNotExistException;
import ru.tsar.university.exceptions.TeacherNotCompetentException;
import ru.tsar.university.exceptions.TeacherNotFreeException;
import ru.tsar.university.exceptions.NotUniqueNameException;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;

@Service
public class LessonService {

	private static final Logger LOG = LoggerFactory.getLogger(LessonService.class);
	private LessonDao lessonDao;

	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public void create(Lesson lesson) {
		try {
			isCapacityEnough(lesson);
			isTeacherCompetent(lesson);
			isNotDayOff(lesson);
			isAuditoriumFree(lesson);
			isTeacherFree(lesson);
			isGroupFree(lesson);
			lessonDao.create(lesson);
		} catch (CapacityNotEnoughException | TeacherNotCompetentException | DayOffException
				| AuditoriumNotFreeException | TeacherNotFreeException | GroupNotFreeException e) {
			LOG.warn(e.getMessage());
		}

	}

	public Lesson getById(int id) {
		return lessonDao.getById(id);
	}

	public List<Lesson> getAll() {
		return lessonDao.getAll();
	}

	public void update(Lesson lesson) {

		try {
			isLessonExist(lesson.getId());
			isCapacityEnough(lesson);
			isTeacherCompetent(lesson);
			isNotDayOff(lesson);
			isAuditoriumFree(lesson);
			isTeacherFree(lesson);
			isGroupFree(lesson);
			lessonDao.update(lesson);
		} catch (LessonNotExistException | CapacityNotEnoughException | TeacherNotCompetentException | DayOffException
				| AuditoriumNotFreeException | TeacherNotFreeException | GroupNotFreeException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void deleteById(int id) {
		try {
			isLessonExist(id);
			lessonDao.deleteById(id);
		} catch (LessonNotExistException e) {
			LOG.warn(e.getMessage());
		}
	}

	public int countStudents(List<Group> groups) {
		return groups.stream().map(Group::getStudents).mapToInt(List::size).sum();
	}

	public void isCapacityEnough(Lesson lesson) throws CapacityNotEnoughException {
		if (lesson.getAuditorium().getCapacity() < countStudents(lesson.getGroups())) {
			throw new CapacityNotEnoughException(lesson.getAuditorium());
		}
	}

	public void isTeacherCompetent(Lesson lesson) {
		if (!lesson.getTeacher().getCourses().contains(lesson.getCourse())) {
			throw new TeacherNotCompetentException(lesson);
		}
	}

	public void isNotDayOff(Lesson lesson) throws DayOffException {
		if (lesson.getDay().getDayOfWeek() == DayOfWeek.SATURDAY | lesson.getDay().getDayOfWeek() == DayOfWeek.SUNDAY) {
			throw new DayOffException(lesson.getDay());
		}
	}

	public void isAuditoriumFree(Lesson lesson) throws AuditoriumNotFreeException {
		Lesson lessonFromDao = lessonDao.getByDayTimeAuditorium(lesson.getDay(), lesson.getTime(),
				lesson.getAuditorium());
		if (lessonFromDao != null) {
			if (lessonFromDao.getId() != lesson.getId()) {
				throw new AuditoriumNotFreeException(lesson);
			}
		}
	}

	public void isTeacherFree(Lesson lesson) throws TeacherNotFreeException {
		Lesson lessonFromDao = lessonDao.getByDayTimeTeacher(lesson.getDay(), lesson.getTime(), lesson.getTeacher());
		if (lessonFromDao != null) {
			if (lessonFromDao.getId() != lesson.getId()) {
				throw new TeacherNotFreeException(lesson);
			}
		}
	}

	public void isGroupFree(Lesson lesson) throws GroupNotFreeException {
		List<Lesson> lessons = lessonDao.getByDayTime(lesson.getDay(), lesson.getTime());
		long count = lessons.stream().filter(l -> l.getId() != lesson.getId()).map(Lesson::getGroups)
				.mapToInt(List::size).sum();
		if (count != 0) {
			throw new GroupNotFreeException(lesson);
		}
	}

	public void isLessonExist(int id) throws LessonNotExistException {
		if (lessonDao.getById(id) == null) {
			throw new LessonNotExistException(id);
		}
	}
}
