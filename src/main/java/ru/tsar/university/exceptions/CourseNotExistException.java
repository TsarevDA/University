package ru.tsar.university.exceptions;

import ru.tsar.university.model.Course;

public class CourseNotExistException extends RuntimeException {

	public CourseNotExistException(Course course) {
		super("This " + course + " is already exist" );
	}

	public CourseNotExistException(int id) {
		super("Course with id = " +id + " is already exist" );
	}

}
