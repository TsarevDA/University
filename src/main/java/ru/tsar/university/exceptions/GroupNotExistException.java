package ru.tsar.university.exceptions;

import ru.tsar.university.model.Group;

public class GroupNotExistException extends RuntimeException {

	public GroupNotExistException(String message) {
		super(message);
	}
}
