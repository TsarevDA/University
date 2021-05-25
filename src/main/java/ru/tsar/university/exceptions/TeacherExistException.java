package ru.tsar.university.exceptions;

import ru.tsar.university.model.Teacher;

public class TeacherExistException extends Exception {

	public TeacherExistException(Teacher teacher) {
		super("This " + teacher + " is already exist" );
	}
	
	public TeacherExistException(int id) {
		super("Teacher with id = " +id + " is already exist" );
	}

}
