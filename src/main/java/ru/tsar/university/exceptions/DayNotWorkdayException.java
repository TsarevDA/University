package ru.tsar.university.exceptions;

import java.time.LocalDate;

public class DayNotWorkdayException extends RuntimeException {

	public DayNotWorkdayException(String message) {
		super(message);
	}

}
