package ru.tsar.university.exceptions;

import ru.tsar.university.model.Auditorium;

public class CapacityNotEnoughException extends RuntimeException {

	public CapacityNotEnoughException(Auditorium auditorium) {
		super("Capacity of auditorium is not enough, auditorium: " + auditorium  );
	}
}
