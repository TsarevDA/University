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
		lesson.setId(0);
		if (checkAuditoriumCapacity(lesson) && checkTeacherCompetence(lesson) && checkDayOff(lesson)
				&& checkAuditoriumFree(lesson) && checkTeacherFree(lesson) && checkGroupFree(lesson)) {
			lessonDao.create(lesson);
		}
	}

	public Lesson getById(int id) {
		Lesson lesson = lessonDao.getById(id);
		if (lesson != null) {
			return lesson;
		} else {
			return null;
		}
	}

	public List<Lesson> getAll() {
		return lessonDao.getAll();
	}

	public void update(Lesson lesson) {
		if (isExistId(lesson.getId()) && checkAuditoriumCapacity(lesson) && checkTeacherCompetence(lesson)
				&& checkDayOff(lesson) && checkAuditoriumFree(lesson) && checkTeacherFree(lesson)
				&& checkGroupFree(lesson)) {
			lessonDao.update(lesson);
		}
	}

	public void deleteById(int id) {
		if (isExistId(id)) {
			lessonDao.deleteById(id);
		}
	}

	public int countStudents(List<Group> groups) {
		return groups.stream().mapToInt(g -> g.getStudents().size()).sum();
	}

	public boolean checkAuditoriumCapacity(Lesson lesson) {
		System.out.println(countStudents(lesson.getGroups()));
		return (lesson.getAuditorium().getCapacity() >= countStudents(lesson.getGroups()));
	}

	public boolean checkTeacherCompetence(Lesson lesson) {
		return (lesson.getTeacher().getCourses().contains(lesson.getCourse()));
	}

	public boolean checkDayOff(Lesson lesson) {
		return (lesson.getDay().getDayOfWeek() != DayOfWeek.SATURDAY
				&& lesson.getDay().getDayOfWeek() != DayOfWeek.SUNDAY);
	}

	public boolean checkAuditoriumFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTimeAuditorium(lesson);
		Lesson lessonById = lessonDao.getById(lesson.getId());
		return (lessons.size() == 0 || lessons.contains(lessonById));
	}

	public boolean checkTeacherFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTimeTeacher(lesson);
		Lesson lessonById = lessonDao.getById(lesson.getId());
		return (lessons.size() == 0 || lessons.contains(lessonById));
	}

	public boolean checkGroupFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTime(lesson);
		Lesson lessonById = lessonDao.getById(lesson.getId());
		long count = 0;
		for (Group group : lesson.getGroups()) {
			count = count + lessons.stream().filter(l -> l.getGroups().contains(group) && l.getId() != lesson.getId())
					.count();
		}
		return count == 0;
	}

	public boolean isExistId(int id) {
		return lessonDao.getById(id) != null;
	}
}
