package ru.tsar.university.exceptions;

import ru.tsar.university.model.Auditorium;

public class CapacityEnoughException extends Exception {

	public CapacityEnoughException(Auditorium auditorium) {
		super("Capacity of auditorium is not enough, auditorium: " + auditorium  );
	}
}
