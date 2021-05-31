package ru.tsar.university.exceptions;

import ru.tsar.university.model.Teacher;

public class TeacherNotExistException extends Exception {

	public TeacherNotExistException(Teacher teacher) {
		super("This " + teacher + " is already exist" );
	}
	
	public TeacherNotExistException(int id) {
		super("Teacher with id = " +id + " is already exist" );
	}

}
