package ru.tsar.university.exceptions;

import java.time.LocalDate;

public class DayOffException extends RuntimeException {
	
	public DayOffException (String message) {
		super(message);
	}

}
