package ru.tsar.university.service;

import java.time.DayOfWeek;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.LessonDao;
import ru.tsar.university.exceptions.AuditoriumFreeException;
import ru.tsar.university.exceptions.CapacityEnoughException;
import ru.tsar.university.exceptions.DayOffException;
import ru.tsar.university.exceptions.GroupExistException;
import ru.tsar.university.exceptions.GroupFreeException;
import ru.tsar.university.exceptions.LessonExistException;
import ru.tsar.university.exceptions.TeacherCompetentException;
import ru.tsar.university.exceptions.TeacherFreeException;
import ru.tsar.university.exceptions.UniqueNameException;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;

@Service
public class LessonService {

	private static final Logger LOG = LoggerFactory.getLogger(LessonService.class);
	private LessonDao lessonDao;

	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public void create(Lesson lesson) throws CapacityEnoughException, TeacherCompetentException, DayOffException,
			AuditoriumFreeException, TeacherFreeException, GroupFreeException {
		if (isCapacityEnough(lesson) && isTeacherCompetent(lesson) && isNotDayOff(lesson) && isAuditoriumFree(lesson)
				&& isTeacherFree(lesson) && isGroupFree(lesson)) {
			lessonDao.create(lesson);
		} else if (!isCapacityEnough(lesson)) {
			throw new CapacityEnoughException(lesson.getAuditorium());
		} else if (!isTeacherCompetent(lesson)) {
			throw new TeacherCompetentException(lesson);
		} else if (!isNotDayOff(lesson)) {
			throw new DayOffException(lesson.getDay());
		} else if (!isAuditoriumFree(lesson)) {
			throw new AuditoriumFreeException(lesson);
		} else if (!isTeacherFree(lesson)) {
			throw new TeacherFreeException(lesson);
		} else if (!isGroupFree(lesson)) {
			throw new GroupFreeException(lesson);
		}

	}

	public Lesson getById(int id) {
		return lessonDao.getById(id);
	}

	public List<Lesson> getAll() {
		return lessonDao.getAll();
	}

	public void update(Lesson lesson) throws LessonExistException, CapacityEnoughException, TeacherCompetentException,
			DayOffException, TeacherFreeException, AuditoriumFreeException, GroupFreeException {
		if (isLessonExist(lesson.getId()) && isCapacityEnough(lesson) && isTeacherCompetent(lesson)
				&& isNotDayOff(lesson) && isAuditoriumFree(lesson) && isTeacherFree(lesson) && isGroupFree(lesson)) {
			lessonDao.update(lesson);
		} else if (!isLessonExist(lesson.getId())) {
			throw new LessonExistException(lesson);
		} else if (!isCapacityEnough(lesson)) {
			throw new CapacityEnoughException(lesson.getAuditorium());
		} else if (!isTeacherCompetent(lesson)) {
			throw new TeacherCompetentException(lesson);
		} else if (!isNotDayOff(lesson)) {
			throw new DayOffException(lesson.getDay());
		} else if (!isAuditoriumFree(lesson)) {
			throw new AuditoriumFreeException(lesson);
		} else if (!isTeacherFree(lesson)) {
			throw new TeacherFreeException(lesson);
		} else if (!isGroupFree(lesson)) {
			throw new GroupFreeException(lesson);
		}
	}

	public void deleteById(int id) throws LessonExistException {
		if (isLessonExist(id)) {
			lessonDao.deleteById(id);
		} else {
			throw new LessonExistException(id);

		}
	}

	public int countStudents(List<Group> groups) {
		return groups.stream().map(Group::getStudents).mapToInt(List::size).sum();
	}

	public boolean isCapacityEnough(Lesson lesson) {
		return lesson.getAuditorium().getCapacity() >= countStudents(lesson.getGroups());
	}

	public boolean isTeacherCompetent(Lesson lesson) {
		return (lesson.getTeacher().getCourses().contains(lesson.getCourse()));
	}

	public boolean isNotDayOff(Lesson lesson) {
		return lesson.getDay().getDayOfWeek() != DayOfWeek.SATURDAY
				&& lesson.getDay().getDayOfWeek() != DayOfWeek.SUNDAY;
	}

	public boolean isAuditoriumFree(Lesson lesson) {
		Lesson lessonFromDao = lessonDao.getByDayTimeAuditorium(lesson.getDay(), lesson.getTime(),
				lesson.getAuditorium());
		if (lessonFromDao == null) {
			return true;
		} else {
			return lessonFromDao.getId() == lesson.getId();
		}
	}

	public boolean isTeacherFree(Lesson lesson) {
		Lesson lessonFromDao = lessonDao.getByDayTimeTeacher(lesson.getDay(), lesson.getTime(), lesson.getTeacher());
		if (lessonFromDao == null) {
			return true;
		} else {
			return lessonFromDao.getId() == lesson.getId();
		}
	}

	public boolean isGroupFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTime(lesson.getDay(), lesson.getTime());
		long count = lessons.stream().filter(l -> l.getId() != lesson.getId()).map(Lesson::getGroups)
				.mapToInt(List::size).sum();
		return count == 0;
	}

	public boolean isLessonExist(int id) {
		return lessonDao.getById(id) != null;
	}
}
