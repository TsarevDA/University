package ru.tsar.model;

import java.time.LocalDate;
import java.util.List;

public class Teacher {
	
	private int id;
	private String firstName;
	private String lastName;
	private String sex;
	private LocalDate birthday;
	private String email;
	private String phone;
	private String address;
	private List<Course> courses;
		
	public Teacher( String firstName, String lastName, String sex, LocalDate birthday, String email,
			String phone, String address, List<Course> courses) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.sex = sex;
		this.birthday = birthday;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.courses = courses;
	}
	
	public Teacher(int id, String firstName, String lastName, String sex, LocalDate birthday, String email,
			String phone, String address, List<Course> courses) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.sex = sex;
		this.birthday = birthday;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
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
