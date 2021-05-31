package ru.tsar.university.exceptions;

import ru.tsar.university.model.Student;

public class StudentNotExistException extends RuntimeException {

	public StudentNotExistException(Student student) {
		super("This " + student + " is already exist" );
	}
	public StudentNotExistException(int id) {
		super("Student with id = " +id + " is already exist" );
	}
}
