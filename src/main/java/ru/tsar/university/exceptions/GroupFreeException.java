package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class GroupFreeException extends Exception {
	
	public GroupFreeException(Lesson lesson) {
		super("One of group: " + lesson.getGroups() + "is busy on day "+ lesson.getDay() + " time: "+ lesson.getTime());
	}

}
