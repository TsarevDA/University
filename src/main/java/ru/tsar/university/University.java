package ru.tsar.university;

import java.util.ArrayList;
import java.util.List;
import ru.tsar.model.Auditorium;
import ru.tsar.model.Course;
import ru.tsar.model.Group;
import ru.tsar.model.Lesson;
import ru.tsar.model.LessonTime;
import ru.tsar.model.Student;
import ru.tsar.model.Teacher;

public class University {

	private List<Teacher> teachers;
	private List<Course> courses;
	private List<Group> groups;
	private List<Student> students;
	private List<Auditorium> auditoriums;
	private List<LessonTime> lessonsTime;
	private List<Lesson> lessons;

	public University() {
		this.teachers = new ArrayList<Teacher>();
		this.courses = new ArrayList<Course>();
		this.groups = new ArrayList<Group>();
		this.students = new ArrayList<Student>();
		this.auditoriums = new ArrayList<Auditorium>();
	}

	public University(List<Teacher> teachers, List<Course> courses, List<Group> groups, List<Student> students,
			List<Auditorium> auditoriums) {
		this.teachers = teachers;
		this.courses = courses;
		this.groups = groups;
		this.students = students;
		this.auditoriums = auditoriums;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void addTeacher(Teacher teacher) {
		teachers.add(teacher);
	}

	public void removeTeacher(Teacher teacher) {
		teachers.remove(teacher);
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void addCourse(Course course) {
		courses.add(course);
	}

	public void removeCourse(Course course) {
		courses.remove(course);
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void addGroup(Group group) {
		groups.add(group);
	}

	public void removeGroup(Group group) {
		groups.remove(group);
	}

	public List<Student> getStudents() {
		return students;
	}

	public void addStudent(Student student) {
		students.add(student);
	}

	public void removeStudent(Student student) {
		students.remove(student);
	}

	public List<Auditorium> getAuditoriums() {
		return auditoriums;
	}

	public void addAuditorium(Auditorium auditorium) {
		auditoriums.add(auditorium);
	}

	public void removeAuditorium(Auditorium auditorium) {
		auditoriums.remove(auditorium);
	}

	public List<LessonTime> getLessonsTime() {
		return lessonsTime;
	}

	public void addLessonsTime(LessonTime lessonTime) {
		this.lessonsTime.add(lessonTime);
	}

	public void setLessonsTime(List<LessonTime> lessonsTime) {
		this.lessonsTime = lessonsTime;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void addLessons(Lesson lesson) {
		this.lessons.add(lesson);
	}

}
