package ru.tsar.university.exceptions;

import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.Teacher;

public class TeacherNotCompetentException extends RuntimeException {

	public TeacherNotCompetentException(String message) {
		super(message);
	}
}
