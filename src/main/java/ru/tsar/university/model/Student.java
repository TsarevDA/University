package ru.tsar.university.model;

import java.time.LocalDate;

public class Student extends Person {
	
	public Student(StudentBuilder builder) {
		setId(builder.id);
		setFirstName(builder.firstName);
		setLastName(builder.lastName);
		setGender(builder.gender);
		setBirthDate(builder.birthDate);
		setEmail(builder.email);
		setPhone(builder.phone);
		setAddress(builder.address);
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
		
		public StudentBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public StudentBuilder setFirstName(String firstName) {
			 
			this.firstName = firstName;
			return this;
		}
		
		public StudentBuilder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		public StudentBuilder setGender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public StudentBuilder setBirthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		public StudentBuilder setEmail(String email) {
			this.email = email;
			return this;
		}
		
		public StudentBuilder setPhone(String phone) {
			this.phone = phone;
			return this;
		}
		public StudentBuilder setAddress(String address) {
			this.address = address;
			return this;
		}
		
		public Student build() {
			return new Student(this);
		}	
		
	}
}
