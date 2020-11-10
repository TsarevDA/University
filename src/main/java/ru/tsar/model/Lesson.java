package ru.tsar.model;

import java.time.LocalDate;
import java.util.List;

public class Lesson {

	private Course course;
	private Teacher teacher;
	private List<Group> group;
	private LocalDate day;
	private LessonTime time;
	private Auditorium auditorium;
	
	public Lesson(Course course, Teacher teacher, List<Group> group, LocalDate day, LessonTime time,
			Auditorium auditorium) {
		this.course = course;
		this.teacher = teacher;
		this.group = group;
		this.day = day;
		this.time = time;
		this.auditorium = auditorium;
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

}
