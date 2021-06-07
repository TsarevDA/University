package ru.tsar.university.exceptions;

import ru.tsar.university.model.LessonTime;

public class TimeNotCorrectException extends RuntimeException {
	
	public TimeNotCorrectException (String message) {
		super(message);
	}
	
}
