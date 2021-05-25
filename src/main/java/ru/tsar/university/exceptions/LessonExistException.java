package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class LessonExistException extends Exception {

	public LessonExistException(Lesson lesson) {
		super("This " + lesson + " is already exist" );
	}
	public LessonExistException(int id) {
		super("Lesson with id = " +id + " is already exist" );
	}
}
