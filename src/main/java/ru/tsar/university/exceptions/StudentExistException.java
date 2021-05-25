package ru.tsar.university.exceptions;

import ru.tsar.university.model.Student;

public class StudentExistException extends Exception {

	public StudentExistException(Student student) {
		super("This " + student + " is already exist" );
	}
	public StudentExistException(int id) {
		super("Student with id = " +id + " is already exist" );
	}
}
