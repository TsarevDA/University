package ru.tsar.university.service;

import java.time.DayOfWeek;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.LessonDao;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;

@Service
public class LessonService {

	private LessonDao lessonDao;
	private final Logger log = LoggerFactory.getLogger(LessonService.class);
	
	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public void create(Lesson lesson) {
		if (isCapacityEnough(lesson) && isTeacherCompetent(lesson) && isNotDayOff(lesson)
				&& isAuditoriumFree(lesson) && isTeacherFree(lesson) && isGroupFree(lesson)) {
			lessonDao.create(lesson);
		} else {
			log.warn("Create error. Not all conditions are met, {}", lesson);
			
		}
	}

	public Lesson getById(int id) {
		return lessonDao.getById(id);
	}

	public List<Lesson> getAll() {
		return lessonDao.getAll();
	}

	public void update(Lesson lesson) {
		if (isLessonExist(lesson.getId()) && isCapacityEnough(lesson) && isTeacherCompetent(lesson)
				&& isNotDayOff(lesson) && isAuditoriumFree(lesson) && isTeacherFree(lesson)
				&& isGroupFree(lesson)) {
			lessonDao.update(lesson);
		} else {
			log.warn("Update error. Not all conditions are met, {}", lesson);
			
		}
	}

	public void deleteById(int id) {
		if (isLessonExist(id)) {
			lessonDao.deleteById(id);
		} else {
			log.warn("deleteById error, lesson id = {} does not exist", id);
			
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
		Lesson lessonFromDao = lessonDao.getByDayTimeAuditorium(lesson.getDay(), lesson.getTime(), lesson.getAuditorium());
		if (lessonFromDao == null) {
			return true;
		} else {
			return lessonFromDao.getId() == lesson.getId();
		}
	}

	public boolean isTeacherFree(Lesson lesson) {
		Lesson lessonFromDao = lessonDao.getByDayTimeTeacher(lesson.getDay(), lesson.getTime(),lesson.getTeacher());
		if (lessonFromDao == null) {
			return true;
		} else {
			return lessonFromDao.getId() == lesson.getId();
		}
	}

	public boolean isGroupFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTime(lesson.getDay(), lesson.getTime());
		long count = lessons.stream().filter(l -> l.getId() != lesson.getId()).map(Lesson::getGroups).mapToInt(List::size).sum();
		return count == 0;
	}

	public boolean isLessonExist(int id) {
		return lessonDao.getById(id) != null;
	}
}
