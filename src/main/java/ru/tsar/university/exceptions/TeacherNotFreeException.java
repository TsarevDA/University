package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class TeacherNotFreeException extends Exception {
	
	public TeacherNotFreeException(Lesson lesson) {
		
		super("Teacher "+ lesson.getTeacher() + " is busy at day: " + lesson.getDay() + ", time " + lesson.getTime());
	}

}
