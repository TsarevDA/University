package ru.tsar.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {

	private List<Course> courses;

	public Teacher(String firstName, String lastName, Gender gender, LocalDate birthday, String email, String phone,
			String address, List<Course> courses) {
		super(firstName, lastName, gender, birthday, email, phone, address);
		this.courses = courses;
	}

	public Teacher(String firstName, String lastName, Gender gender, LocalDate birthday, String email, String phone,
			String address) {
		super(firstName, lastName, gender, birthday, email, phone, address);
		courses = new ArrayList<Course>();
	}

	public Teacher(int id, String firstName, String lastName, Gender gender, LocalDate birthday, String email,
			String phone, String address) {
		super(id, firstName, lastName, gender, birthday, email, phone, address);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courses == null) ? 0 : courses.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Teacher other = (Teacher) obj;
		if (courses == null) {
			if (other.courses != null)
				return false;
		} else if (!courses.equals(other.courses))
			return false;
		return true;
	}
}
