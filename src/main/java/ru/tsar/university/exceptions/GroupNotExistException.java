package ru.tsar.university.exceptions;

import ru.tsar.university.model.Group;

public class GroupNotExistException extends RuntimeException {

	public GroupNotExistException(Group group) {
		super("This " + group + " is already exist" );
	}
	public GroupNotExistException(int id) {
		super("Group with id = " +id + " is already exist" );
	}
}
