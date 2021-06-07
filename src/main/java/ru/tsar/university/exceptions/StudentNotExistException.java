package ru.tsar.university.exceptions;

import ru.tsar.university.model.Student;

public class StudentNotExistException extends RuntimeException {

	public StudentNotExistException(String message) {
		super(message);
	}
}
