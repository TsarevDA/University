package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class AuditoriumFreeException extends Exception {
	
	public AuditoriumFreeException (Lesson lesson) {
		super("Auditorium " + lesson.getAuditorium() + " is busy at this day: " + lesson.getDay() + ", time" + lesson.getTime());
	}

}
