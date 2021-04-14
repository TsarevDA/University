package ru.tsar.university.service;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.LessonDao;
import ru.tsar.university.dao.TeacherDao;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;

@Service
public class LessonService {

	private LessonDao lessonDao;
	private AuditoriumDao auditoriumDao;
	private TeacherDao teacherDao;
	private GroupDao groupDao;

	public LessonService(LessonDao lessonDao, AuditoriumDao auditoriumDao, TeacherDao teacherDao, GroupDao groupDao) {
		this.lessonDao = lessonDao;
		this.auditoriumDao = auditoriumDao;
		this.teacherDao = teacherDao;
		this.groupDao = groupDao;
	}

	public void create(Lesson lesson) {

		if (checkAuditoriumCapacity(lesson) && checkTeacherCompetence(lesson) && checkDayOff(lesson)
				&& checkAuditoriumFree(lesson) && checkTeacherFree(lesson) && checkGroupFree(lesson)) {
			lessonDao.create(lesson);
		}
	}

	public Lesson getById(int id) {
		if (lessonDao.getById(id) != null) {
			return lessonDao.getById(id);
		} else {
			return null;
		}
	}

	public List<Lesson> getAll() {
		return lessonDao.getAll();
	}

	public void update(Lesson lesson) {
		if (lessonDao.getById(lesson.getId()) != null && checkAuditoriumCapacity(lesson)
				&& checkTeacherCompetence(lesson) && checkDayOff(lesson) && checkAuditoriumFree(lesson)
				&& checkTeacherFree(lesson) && checkGroupFree(lesson)) {
			lessonDao.update(lesson);
		}
	}

	public void deleteById(int id) {
		if (lessonDao.getById(id) != null) {
			lessonDao.deleteById(id);
		}
	}

	public int countStudents(List<Group> groups) {
		int counter = 0;
		if (groups != null) {
			for (Group group : groups) {
				if (group.getStudents() != null) {
					counter = counter + group.getStudents().size();
				}
			}
		}
		return counter;
	}

	public boolean checkAuditoriumCapacity(Lesson lesson) {
		return (lesson.getAuditorium().getCapacity() >= countStudents(lesson.getGroups())) ? true : false;
	}

	public boolean checkTeacherCompetence(Lesson lesson) {
		return (lesson.getTeacher().getCourses().contains(lesson.getCourse())) ? true : false;
	}

	public boolean checkDayOff(Lesson lesson) {
		return (lesson.getDay().getDayOfWeek() != (DayOfWeek.SATURDAY)
				&& lesson.getDay().getDayOfWeek() != DayOfWeek.SUNDAY) ? true : false;
	}

	public boolean checkAuditoriumFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTime(lesson);
		return (lessons.stream().filter(l -> l.getAuditorium().equals(lesson.getAuditorium())).count() == 0) ? true
				: false;
	}

	public boolean checkTeacherFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTime(lesson);
		return (lessons.stream().filter(l -> l.getTeacher().equals(lesson.getTeacher())).count() == 0) ? true : false;
	}

	public boolean checkGroupFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTime(lesson);
		long count = 0;
		for (Group group : lesson.getGroups()) {
			count = count + lessons.stream().filter(l -> l.getGroups().contains(group)).count();
		}

		return count == 0 ? true : false;
	}
}
