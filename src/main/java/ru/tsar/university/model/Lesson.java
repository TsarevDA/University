package ru.tsar.university.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import ru.tsar.university.model.Auditorium.AuditoriumBuilder;

public class Lesson {

	private int id;
	private Course course;
	private Teacher teacher;
	private List<Group> groups;
	private @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate day;
	private LessonTime time;
	private Auditorium auditorium;

	private Lesson() {
	}

	public int getId() {
		return id;
	}

	public Course getCourse() {
		return course;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public LocalDate getDay() {
		return day;
	}

	public LessonTime getLessonTime() {
		return time;
	}

	public Auditorium getAuditorium() {
		return auditorium;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public void setGroups(List<Group> group) {
		this.groups = group;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public void setLessonTime(LessonTime time) {
		this.time = time;
	}

	public void setAuditorium(Auditorium auditorium) {
		this.auditorium = auditorium;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditorium == null) ? 0 : auditorium.hashCode());
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + id;
		result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		Lesson other = (Lesson) obj;
		if (auditorium == null) {
			if (other.auditorium != null)
				return false;
		} else if (!auditorium.equals(other.auditorium))
			return false;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		if (groups == null) {
			if (other.groups != null)
				return false;
		} else if (!groups.equals(other.groups))
			return false;
		if (id != other.id)
			return false;
		if (teacher == null) {
			if (other.teacher != null)
				return false;
		} else if (!teacher.equals(other.teacher))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Lesson [id=" + id + ", course=" + course + ", teacher=" + teacher + ", group=" + groups + ", day=" + day
				+ ", time=" + time + ", auditorium=" + auditorium + "]";
	}

	public static LessonBuilder builder() {
		return new LessonBuilder();
	}

	public static class LessonBuilder {

		private int id;
		private Course course;
		private Teacher teacher;
		private List<Group> group;
		private LocalDate day;
		private LessonTime time;
		private Auditorium auditorium;

		public LessonBuilder() {
		}

		public LessonBuilder id(int id) {
			this.id = id;
			return this;
		}

		public LessonBuilder auditorium(Auditorium auditorium) {
			this.auditorium = auditorium;
			return this;
		}

		public LessonBuilder time(LessonTime time) {
			this.time = time;
			return this;
		}

		public LessonBuilder day(LocalDate day) {
			this.day = day;
			return this;
		}

		public LessonBuilder group(List<Group> group) {
			this.group = group;
			return this;
		}

		public LessonBuilder teacher(Teacher teacher) {
			this.teacher = teacher;
			return this;
		}

		public LessonBuilder course(Course course) {
			this.course = course;
			return this;
		}

		public Lesson build() {
			Lesson lesson = new Lesson();
			lesson.setId(id);
			lesson.setCourse(course);
			lesson.setTeacher(teacher);
			lesson.setGroups(group);
			lesson.setDay(day);
			lesson.setLessonTime(time);
			lesson.setAuditorium(auditorium);
			return lesson;
		}

	}

}
