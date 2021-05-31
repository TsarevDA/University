package ru.tsar.university.exceptions;

import ru.tsar.university.model.Auditorium;

public class AuditroiumNotExistException extends RuntimeException {

	public AuditroiumNotExistException(Auditorium auditorium) {
		super("This " + auditorium + " is already exist" );
	}
	
	public AuditroiumNotExistException(int id) {
		super("Auditorium with id = " +id + " is already exist" );
	}

}
