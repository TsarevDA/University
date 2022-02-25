package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class GroupNotFreeException extends RuntimeException {

	public GroupNotFreeException(String message) {
		super(message);
	}
}
