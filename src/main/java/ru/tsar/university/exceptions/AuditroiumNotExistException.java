package ru.tsar.university.exceptions;

import ru.tsar.university.model.Auditorium;

public class AuditroiumNotExistException extends RuntimeException {

	public AuditroiumNotExistException(String message) {
		super(message);
	}

}
