package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class LessonNotExistException extends RuntimeException {

	public LessonNotExistException(Lesson lesson) {
		super("This " + lesson + " is already exist" );
	}
	public LessonNotExistException(int id) {
		super("Lesson with id = " +id + " is already exist" );
	}
}
