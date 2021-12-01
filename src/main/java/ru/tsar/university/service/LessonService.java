package ru.tsar.university.service;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.LessonDao;
import ru.tsar.university.exceptions.AuditoriumNotFreeException;
import ru.tsar.university.exceptions.CapacityNotEnoughException;
import ru.tsar.university.exceptions.DayNotWorkdayException;
import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.exceptions.GroupNotFreeException;
import ru.tsar.university.exceptions.TeacherNotCompetentException;
import ru.tsar.university.exceptions.TeacherNotFreeException;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;

@Service
public class LessonService {

	private LessonDao lessonDao;

	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public void create(Lesson lesson) {
		verifyCapacityEnough(lesson);
		verifyTeacherCompetence(lesson);
		verifyNotDayOff(lesson);
		verifyAuditoriumFree(lesson);
		verifyTeacherFree(lesson);
		verifyGroupFree(lesson);
		lessonDao.create(lesson);
	}

	public Lesson getById(int id) {
		return lessonDao.getById(id);
	}

	public Page<Lesson> getAll(Pageable pageable) {
		return lessonDao.getAll(pageable);
	}

	public void update(Lesson lesson) {
		verifyLessonExistence(lesson.getId());
		verifyCapacityEnough(lesson);
		verifyTeacherCompetence(lesson);
		verifyNotDayOff(lesson);
		verifyAuditoriumFree(lesson);
		verifyTeacherFree(lesson);
		verifyGroupFree(lesson);
		lessonDao.update(lesson);
	}

	public void deleteById(int id) {
		verifyLessonExistence(id);
		lessonDao.deleteById(id);
	}

	public int countStudents(List<Group> groups) {
		return groups.stream().map(Group::getStudents).mapToInt(List::size).sum();
	}

	public void verifyCapacityEnough(Lesson lesson) {
		int studentsAmount = countStudents(lesson.getGroups());
		if (lesson.getAuditorium().getCapacity() < studentsAmount) {
			throw new CapacityNotEnoughException(
					lesson.getAuditorium() + " capacity is not enough for " + studentsAmount + " students ");
		}
	}

	public void verifyTeacherCompetence(Lesson lesson) {
		if (!lesson.getTeacher().getCourses().contains(lesson.getCourse())) {
			throw new TeacherNotCompetentException(
					lesson.getTeacher() + "can't teach this course: " + lesson.getCourse());
		}
	}

	public void verifyNotDayOff(Lesson lesson) {
		if (lesson.getDay().getDayOfWeek() == DayOfWeek.SATURDAY | lesson.getDay().getDayOfWeek() == DayOfWeek.SUNDAY) {
			throw new DayNotWorkdayException("Day " + lesson.getDay() + " is dayoff");
		}
	}

	public void verifyAuditoriumFree(Lesson lesson) {
		Lesson lessonFromDao = lessonDao.getByDayTimeAuditorium(lesson.getDay(), lesson.getTime(),
				lesson.getAuditorium());
		if (lessonFromDao != null) {
			if (lessonFromDao.getId() != lesson.getId()) {
				throw new AuditoriumNotFreeException("Auditorium: " + lesson.getAuditorium() + " busy on day "
						+ lesson.getDay() + " time " + lesson.getTime());
			}
		}
	}

	public void verifyTeacherFree(Lesson lesson) {
		Lesson lessonFromDao = lessonDao.getByDayTimeTeacher(lesson.getDay(), lesson.getTime(), lesson.getTeacher());
		if (lessonFromDao != null) {
			if (lessonFromDao.getId() != lesson.getId()) {
				throw new TeacherNotFreeException("Teacher: " + lesson.getTeacher() + " busy on day " + lesson.getDay()
						+ " time " + lesson.getTime());
			}
		}
	}

	public void verifyGroupFree(Lesson lesson) {
		List<Lesson> lessons = lessonDao.getByDayTime(lesson.getDay(), lesson.getTime());
		long count = lessons.stream().filter(l -> l.getId() != lesson.getId()).map(Lesson::getGroups)
				.mapToInt(List::size).sum();
		if (count != 0) {
			throw new GroupNotFreeException("One of this groups: " + lesson.getGroups() + " busy on day "
					+ lesson.getDay() + " time " + lesson.getTime());
		}
	}

	public void verifyLessonExistence(int id) {
		if (lessonDao.getById(id) == null) {
			throw new EntityNotFoundException("Lesson with id = " + id + " does not exist");
		}
	}
}
