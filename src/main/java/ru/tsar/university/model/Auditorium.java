package ru.tsar.university.model;

public class Auditorium {

	private int id;
	private String name;
	private int capacity;
	private AuditoriumBuilder builder;

	private Auditorium() {
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "Auditorium [id=" + id + ", name=" + name + ", capacity=" + capacity + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
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
		Auditorium other = (Auditorium) obj;
		if (capacity != other.capacity)
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

	public static AuditoriumBuilder builder() {
		return new AuditoriumBuilder();
	}

	public static class AuditoriumBuilder {

		private int id;
		private String name;
		private int capacity;

		public AuditoriumBuilder() {
		}

		public AuditoriumBuilder id(int id) {
			this.id = id;
			return this;
		}

		public AuditoriumBuilder name(String name) {
			this.name = name;
			return this;
		}

		public AuditoriumBuilder capacity(int capacity) {
			this.capacity = capacity;
			return this;
		}

		public Auditorium build() {
			Auditorium audtorium = new Auditorium();
			audtorium.setId(id);
			audtorium.setName(name);
			audtorium.setCapacity(capacity);
			return audtorium;
		}
	}

}
