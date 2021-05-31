package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.Teacher;

public class TeacherNotCompetentException extends RuntimeException {

	public TeacherNotCompetentException(Lesson lesson) {
			super("This teacher "+ lesson.getTeacher()+" is not competent to teach this course, id = "+lesson.getCourse());	
	}
}
