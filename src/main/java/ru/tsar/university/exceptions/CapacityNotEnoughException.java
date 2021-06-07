package ru.tsar.university.exceptions;

import ru.tsar.university.model.Auditorium;

public class CapacityNotEnoughException extends RuntimeException {

	public CapacityNotEnoughException(String message) {
		super(message);
	}
}
