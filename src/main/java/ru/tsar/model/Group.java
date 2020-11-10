package ru.tsar.model;

import java.util.List;

public class Group {

	private int id;
	private String name;
	private List<Student> students;
	
	public Group(String name) {
		this.name = name;
	}
	
	public Group(String name, List<Student> students) {
		this.name = name;
		this.students = students;
	}
	
	public Group(int id, String name, List<Student> students) {
		this.id = id;
		this.name = name;
		this.students = students;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}
	
}
