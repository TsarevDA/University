package ru.tsar.university.model;

import java.time.LocalDate;
import java.util.List;

public class Teacher {

	private int id;
	private String firstName;
	private String lastName;
	private Gender gender;
	private LocalDate birthDate;
	private String email;
	private String phone;
	private String address;
	private List<Course> courses;

	public Teacher(String firstName, String lastName, Gender gender, LocalDate birthday, String email, String phone,
			String address, List<Course> courses) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDate = birthday;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.courses = courses;
	}

	public Teacher(int id, String firstName, String lastName, Gender gender, LocalDate birthday, String email,
			String phone, String address, List<Course> courses) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDate = birthday;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.courses = courses;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

}
