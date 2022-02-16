package ru.tsar.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ru.tsar.university.model.Student.StudentBuilder;

public class Teacher extends Person {

	private List<Course> courses;

	private Teacher() {
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public String toString() {

		return super.toString() + "Teacher [courses=" + courses + "]";
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

	public static TeacherBuilder builder() {
		return new TeacherBuilder();
	}

	public static class TeacherBuilder {

		private int id;
		private String firstName;
		private String lastName;
		private Gender gender;
		private LocalDate birthDate;
		private String email;
		private String phone;
		private String address;
		private List<Course> courses;

		public TeacherBuilder() {
		}

		public TeacherBuilder id(int id) {
			this.id = id;
			return this;
		}

		public TeacherBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public TeacherBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public TeacherBuilder gender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public TeacherBuilder birthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		public TeacherBuilder email(String email) {
			this.email = email;
			return this;
		}

		public TeacherBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public TeacherBuilder address(String address) {
			this.address = address;
			return this;
		}

		public TeacherBuilder courses(List<Course> courses) {
			this.courses = courses;
			return this;
		}

		public Teacher build() {
			Teacher teacher = new Teacher();
			teacher.setId(id);
			teacher.setFirstName(firstName);
			teacher.setLastName(lastName);
			teacher.setGender(gender);
			teacher.setBirthDate(birthDate);
			teacher.setEmail(email);
			teacher.setPhone(phone);
			teacher.setAddress(address);
			teacher.setCourses(courses);
			return teacher;
		}

	}
}
