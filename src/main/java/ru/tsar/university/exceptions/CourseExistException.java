package ru.tsar.university.exceptions;

import ru.tsar.university.model.Course;

public class CourseExistException extends Exception {

	public CourseExistException(Course course) {
		super("This " + course + " is already exist" );
	}

	public CourseExistException(int id) {
		super("Course with id = " +id + " is already exist" );
	}

}
