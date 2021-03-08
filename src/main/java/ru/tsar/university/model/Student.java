package ru.tsar.university.model;

import java.time.LocalDate;

import ru.tsar.university.model.LessonTime.LessonTimeBuilder;

public class Student extends Person {

	private Student() {
	}

	public static StudentBuilder builder() {
		return new StudentBuilder();
	}

	public static class StudentBuilder {

		private int id;
		private String firstName;
		private String lastName;
		private Gender gender;
		private LocalDate birthDate;
		private String email;
		private String phone;
		private String address;

		public StudentBuilder() {

		}

		public StudentBuilder id(int id) {
			this.id = id;
			return this;
		}

		public StudentBuilder firstName(String firstName) {

			this.firstName = firstName;
			return this;
		}

		public StudentBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public StudentBuilder gender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public StudentBuilder birthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		public StudentBuilder email(String email) {
			this.email = email;
			return this;
		}

		public StudentBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public StudentBuilder address(String address) {
			this.address = address;
			return this;
		}

		public Student build() {
			Student student = new Student();
			student.setId(id);
			student.setFirstName(firstName);
			student.setLastName(lastName);
			student.setGender(gender);
			student.setBirthDate(birthDate);
			student.setEmail(email);
			student.setPhone(phone);
			student.setAddress(address);
			return student;
		}

	}
}
