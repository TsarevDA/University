package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class AuditoriumNotFreeException extends RuntimeException {

	public AuditoriumNotFreeException(String message) {
		super(message);
	}

}
