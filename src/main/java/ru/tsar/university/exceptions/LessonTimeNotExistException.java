package ru.tsar.university.exceptions;

import ru.tsar.university.model.LessonTime;

public class LessonTimeNotExistException extends RuntimeException {

	public LessonTimeNotExistException(String message) {
		super(message);
	}
}
