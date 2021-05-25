package ru.tsar.university.exceptions;

import ru.tsar.university.model.Auditorium;

public class AuditroiumExistException extends Exception {

	public AuditroiumExistException(Auditorium auditorium) {
		super("This " + auditorium + " is already exist" );
	}
	
	public AuditroiumExistException(int id) {
		super("Auditorium with id = " +id + " is already exist" );
	}

}
