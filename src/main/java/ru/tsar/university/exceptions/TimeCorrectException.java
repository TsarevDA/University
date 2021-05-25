package ru.tsar.university.exceptions;

import ru.tsar.university.model.LessonTime;

public class TimeCorrectException extends Exception {
	
	public TimeCorrectException (LessonTime lessonTime) {
		super("LessonTime format is not correct " + lessonTime);
	}
	
}
