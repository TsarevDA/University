package ru.tsar.university.model;

import ru.tsar.university.model.Auditorium.AuditoriumBuilder;

public class Course {

	private int id;
	private String name;
	private String description;

	private Course() {
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Course other = (Course) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public static CourseBuilder builder() {
		return new CourseBuilder();
	}

	public static class CourseBuilder {

		private int id;
		private String name;
		private String description;

		public CourseBuilder() {
		}

		public CourseBuilder id(int id) {
			this.id = id;
			return this;
		}

		public CourseBuilder name(String name) {
			this.name = name;
			return this;
		}

		public CourseBuilder description(String description) {
			this.description = description;
			return this;
		}

		public Course build() {
			Course course = new Course();
			course.setId(id);
			course.setName(name);
			course.setDescription(description);
			return course;
		}

	}

}
