package ru.tsar.university.exceptions;

import ru.tsar.university.model.LessonTime;

public class LessonTimeExistException extends Exception {

	public LessonTimeExistException(LessonTime lessonTime) {
		super("This " + lessonTime + " is already exist" );
	}
	public LessonTimeExistException(int id) {
		super("LessonTime with id = " +id + " is already exist" );
	}
}
