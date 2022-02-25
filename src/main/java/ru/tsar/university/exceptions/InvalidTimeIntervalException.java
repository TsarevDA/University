package ru.tsar.university.exceptions;

import ru.tsar.university.model.LessonTime;

public class InvalidTimeIntervalException extends RuntimeException {

	public InvalidTimeIntervalException(String message) {
		super(message);
	}

}
