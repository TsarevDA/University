package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;

public class GroupNotFreeException extends Exception {
	
	public GroupNotFreeException(Lesson lesson) {
		super("One of group: " + lesson.getGroups() + "is busy on day "+ lesson.getDay() + " time: "+ lesson.getTime());
	}

}
