package ru.tsar.university.exceptions;

import ru.tsar.university.model.LessonTime;

public class LessonTimeNotExistException extends RuntimeException {

	public LessonTimeNotExistException(LessonTime lessonTime) {
		super("This " + lessonTime + " is already exist" );
	}
	public LessonTimeNotExistException(int id) {
		super("LessonTime with id = " +id + " is already exist" );
	}
}
