package ru.tsar.university.model;

import java.time.LocalDate;
import java.util.List;

public class Lesson {

	private int id;
	private Course course;
	private Teacher teacher;
	private List<Group> group;
	private LocalDate day;
	private LessonTime time;
	private Auditorium auditorium;

	public Lesson(int id, Course course, Teacher teacher, List<Group> group, LocalDate day, LessonTime time,
			Auditorium auditorium) {
		this.id = id;
		this.course = course;
		this.teacher = teacher;
		this.group = group;
		this.day = day;
		this.time = time;
		this.auditorium = auditorium;
	}

	public Lesson(Course course, Teacher teacher, List<Group> group, LocalDate day, LessonTime time,
			Auditorium auditorium) {
		this.course = course;
		this.teacher = teacher;
		this.group = group;
		this.day = day;
		this.time = time;
		this.auditorium = auditorium;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public List<Group> getGroup() {
		return group;
	}

	public void setGroup(List<Group> group) {
		this.group = group;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public LessonTime getTime() {
		return time;
	}

	public void setTime(LessonTime time) {
		this.time = time;
	}

	public Auditorium getAuditorium() {
		return auditorium;
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
		result = prime * result + ((group == null) ? 0 : group.hashCode());
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
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
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

}
