package ru.tsar.university.exceptions;

public class NotUniqueNameException extends RuntimeException {
	
	public NotUniqueNameException(String name) {
		super("The name is not unique: "+ name );
	}

}
