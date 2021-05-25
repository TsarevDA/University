package ru.tsar.university.exceptions;

public class UniqueNameException extends Exception {
	
	public UniqueNameException(String name) {
		super("The name is not unique: "+ name );
	}

}
