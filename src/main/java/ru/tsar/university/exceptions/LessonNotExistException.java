package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class LessonNotExistException extends RuntimeException {

	public LessonNotExistException(String message) {
		super(message);
	}
}
