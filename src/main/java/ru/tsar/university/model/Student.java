package ru.tsar.university.model;

import java.time.LocalDate;

public class Student extends Person {

	public Student(String firstName, String lastName, Gender gender, LocalDate birthday, String email, String phone,
			String address) {
		super(firstName, lastName, gender, birthday, email, phone, address);

	}

	public Student(int id, String firstName, String lastName, Gender gender, LocalDate birthday, String email,
			String phone, String address) {
		super(id, firstName, lastName, gender, birthday, email, phone, address);
	}

}
