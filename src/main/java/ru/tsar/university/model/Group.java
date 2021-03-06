package ru.tsar.university.model;

import java.util.List;

import ru.tsar.university.model.Auditorium.AuditoriumBuilder;

public class Group {

	private int id;
	private String name;
	private List<Student> students;

	private Group() {

	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((students == null) ? 0 : students.hashCode());
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
		Group other = (Group) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (students == null) {
			if (other.students != null)
				return false;
		} else if (!students.equals(other.students))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", students=" + students + "]";
	}

	public static GroupBuilder builder() {
		return new GroupBuilder();
	}

	public static class GroupBuilder {

		private int id;
		private String name;
		private List<Student> students;

		public GroupBuilder() {

		}

		public GroupBuilder id(int id) {
			this.id = id;
			return this;

		}

		public GroupBuilder name(String name) {
			this.name = name;
			return this;
		}

		public GroupBuilder students(List<Student> students) {
			this.students = students;
			return this;
		}

		public Group build() {
			Group group = new Group();
			group.setId(id);
			group.setName(name);
			group.setStudents(students);
			return group;
		}
	}

}
