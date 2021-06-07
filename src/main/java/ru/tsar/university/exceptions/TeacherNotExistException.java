package ru.tsar.university.exceptions;

import ru.tsar.university.model.Teacher;

public class TeacherNotExistException extends RuntimeException {

	public TeacherNotExistException(String message) {
		super(message);
	}

}
