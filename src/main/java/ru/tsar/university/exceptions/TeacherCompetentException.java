package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.Teacher;

public class TeacherCompetentException extends Exception {

	public TeacherCompetentException(Lesson lesson) {
			super("This teacher "+ lesson.getTeacher()+" is not competent to teach this course, id = "+lesson.getCourse());	
	}
}
