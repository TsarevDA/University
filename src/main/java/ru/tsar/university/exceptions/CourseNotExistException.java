package ru.tsar.university.exceptions;

import ru.tsar.university.model.Course;

public class CourseNotExistException extends RuntimeException {

	public CourseNotExistException(String message) {
		super(message);
	}

}
