package ru.tsar.university.exceptions;

import java.time.LocalDate;

public class DayOffException extends Exception {
	
	public DayOffException (LocalDate day) {
		super("This day " + day + "is non-working");
	}

}
