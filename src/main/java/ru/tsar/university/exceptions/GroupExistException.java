package ru.tsar.university.exceptions;

import ru.tsar.university.model.Group;

public class GroupExistException extends Exception {

	public GroupExistException(Group group) {
		super("This " + group + " is already exist" );
	}
	public GroupExistException(int id) {
		super("Group with id = " +id + " is already exist" );
	}
}
