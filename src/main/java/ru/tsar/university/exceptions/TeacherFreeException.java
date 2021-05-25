package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class TeacherFreeException extends Exception {
	
	public TeacherFreeException(Lesson lesson) {
		
		super("Teacher "+ lesson.getTeacher() + " is busy at day: " + lesson.getDay() + ", time " + lesson.getTime());
	}

}
