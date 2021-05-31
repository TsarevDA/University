package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class AuditoriumNotFreeException extends RuntimeException {
	
	public AuditoriumNotFreeException (Lesson lesson) {
		super("Auditorium " + lesson.getAuditorium() + " is busy at this day: " + lesson.getDay() + ", time" + lesson.getTime());
	}

}
