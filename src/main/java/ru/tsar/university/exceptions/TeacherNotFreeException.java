package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class TeacherNotFreeException extends RuntimeException {
	
	public TeacherNotFreeException(String message) {
		super(message);
	}

}
