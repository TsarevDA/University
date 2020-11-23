package ru.tsar.university.model;

import java.time.LocalDate;
import java.util.List;

public class Teacher extends Person {

	private List<Course> courses;

	public Teacher(String firstName, String lastName, Gender gender, LocalDate birthday, String email, String phone,
			String address, List<Course> courses) {
		super(firstName, lastName, gender, birthday, email, phone, address);
		this.courses = courses;
	}

	public Teacher(int id, String firstName, String lastName, Gender gender, LocalDate birthday, String email,
			String phone, String address, List<Course> courses) {
		super(id, firstName, lastName, gender, birthday, email, phone, address);
		this.courses = courses;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
}
