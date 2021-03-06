package ru.tsar.university;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;

@Component
public class University {

	private List<Teacher> teachers;
	private List<Course> courses;
	private List<Group> groups;
	private List<Student> students;
	private List<Auditorium> auditoriums;
	private List<Lesson> lessons;
	private List<LessonTime> lessonsTime;

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

	public void deleteTeacher(Teacher teacher) {
		teachers.remove(teacher);
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void addCourse(Course course) {
		courses.add(course);
	}

	public void deleteCourse(Course course) {
		courses.remove(course);
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void addGroup(Group group) {
		groups.add(group);
	}

	public void deleteGroup(Group group) {
		groups.remove(group);
	}

	public List<Student> getStudents() {
		return students;
	}

	public void addStudent(Student student) {
		students.add(student);
	}

	public void deleteStudent(Student student) {
		students.remove(student);
	}

	public List<Auditorium> getAuditoriums() {
		return auditoriums;
	}

	public void addAuditorium(Auditorium auditorium) {
		auditoriums.add(auditorium);
	}

	public void deleteAuditorium(Auditorium auditorium) {
		auditoriums.remove(auditorium);
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void addLesson(Lesson lesson) {
		lessons.add(lesson);
	}

	public void deleteLesson(Lesson lesson) {
		lessons.remove(lesson);
	}

	public List<LessonTime> getLessonsTime() {
		return lessonsTime;
	}

	public void addLessonTime(LessonTime lessonTime) {
		this.lessonsTime.add(lessonTime);
	}

	public void deleteLessonTime(LessonTime lessonTime) {
		this.lessonsTime.remove(lessonTime);
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public void setLessonsTime(List<LessonTime> lessonsTime) {
		this.lessonsTime = lessonsTime;
	}

}
