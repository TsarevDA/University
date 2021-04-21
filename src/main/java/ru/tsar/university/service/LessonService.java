package ru.tsar.university.service;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.stereotype.Service;

import ru.tsar.university.dao.LessonDao;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;

@Service
public class LessonService {

	private LessonDao lessonDao;

	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public void create(Lesson lesson) {
		if (isCapacityEnough(lesson) && isTeacherCompetent(lesson) && isNotDayOff(lesson)
				&& isAuditoriumFree(lesson) && isTeacherFree(lesson) && isGroupFree(lesson)) {
			lessonDao.create(lesson);
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
		}
	}

	public void deleteById(int id) {
		if (isLessonExist(id)) {
			lessonDao.deleteById(id);
		}
	}

	public int countStudents(List<Group> groups) {
		return groups.stream().mapToInt(g -> g.getStudents().size()).sum();
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
		List<Lesson> lessons = lessonDao.getByDayTimeAuditorium(lesson.getDay(), lesson.getTime(), lesson.getAuditorium());
		return lessons.size() == 0 || lessons.contains(lessonDao.getById(lesson.getId()));
	}

	public boolean isTeacherFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTimeTeacher(lesson.getDay(), lesson.getTime(),lesson.getTeacher());
		return lessons.size() == 0 || lessons.get(0).getId() == lesson.getId();
	}

	public boolean isGroupFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTime(lesson.getDay(), lesson.getTime());
		long count = 0;
		for (Group group : lesson.getGroups()) {
			count = count + lessons.stream().filter(l -> l.getGroups().contains(group) && l.getId() != lesson.getId())
					.count();
		}
		return count == 0;
	}

	public boolean isLessonExist(int id) {
		return lessonDao.getById(id) != null;
	}
}
