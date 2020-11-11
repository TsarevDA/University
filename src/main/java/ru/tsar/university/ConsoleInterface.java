package ru.tsar.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;
import ru.tsar.university.model.Gender;

public class ConsoleInterface {

	private University university;
	final private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public ConsoleInterface(University university) {
		this.university = university;
	}

	public void startMenu() {

		Scanner scanner = new Scanner(System.in);
		String line = "";

		while (line.equals("q")) {
			System.out.println("for exit press 'q', for help press 'h'");
			line = scanner.next();

			if (line.equals("h")) {
				// System.out.println(helpMenu());
			}
			if (line.equals("as")) {
				addStudent();
			}
			if (line.equals("rs")) {
				removeStudent();
			}
			if (line.equals("ac")) {
				addCourse();
			}
			if (line.equals("at")) {
				addTeacher();
			}
			if (line.equals("atc")) {

			}
			if (line.equals("clt")) {
				createLessonTime();
			}

			if (line.equals("q")) {
				System.out.println("Exit");
				break;
			}
		}
	}

	private void addStudent() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter student first name:");
		String firstName = scanner.next();
		System.out.println("Enter student last name:");
		String lastName = scanner.next();
		System.out.println("Enter student gender (male,female):");
		String stringGender = scanner.next();
		Gender gender = Gender.valueOf(stringGender);
		System.out.println("Enter student birthday in format \"yyyy-MM-dd\":");
		String birthday = scanner.next();
		LocalDate date = LocalDate.parse(birthday, dateFormatter);
		System.out.println("Enter student email:");
		String email = scanner.next();
		System.out.println("Enter student phone:");
		String phone = scanner.next();
		System.out.println("Enter student address:");
		String address = scanner.next();

		Student student = new Student(firstName, lastName, gender, date, email, phone, address);
		System.out.println(student);
		university.addStudent(student);
	}

	private void removeStudent() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter student id you want to remove:");
		int id = scanner.nextInt();
		List<Student> students = university.getStudents();
		List<Student> studentsForRemoving = students.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		for (Student student : studentsForRemoving) {
			university.removeStudent(student);
		}
	}

	private void addCourse() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter course name:");
		String name = scanner.next();
		System.out.println("Enter course description:");
		String description = scanner.next();
		Course course = new Course(name, description);
		university.addCourse(course);
	}

	private void removeCourse() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter course id you want to remove:");
		int id = scanner.nextInt();
		List<Course> courses = university.getCourses();
		List<Course> coursesForRemoving = courses.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		for (Course course : coursesForRemoving) {
			university.removeCourse(course);
		}
	}

	private void addTeacher() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter teacher first name:");
		String firstName = scanner.next();
		System.out.println("Enter teacher last name:");
		String lastName = scanner.next();
		System.out.println("Enter student gender (male,female):");
		String stringGender = scanner.next();
		Gender gender = Gender.valueOf(stringGender);
		System.out.println("Enter teacher birthday in format \"yyyy-MM-dd\":");
		String birthday = scanner.next();
		LocalDate date = LocalDate.parse(birthday, dateFormatter);
		System.out.println("Enter teacher email:");
		String email = scanner.next();
		System.out.println("Enter teacher phone:");
		String phone = scanner.next();
		System.out.println("Enter teacher address:");
		String address = scanner.next();
		System.out.println("Enter teacher's courses separated by commas( For example: 1,2,5 ):");
		String course = scanner.next();
		List<String> coursesNames = Arrays.stream(course.split(",")).collect(Collectors.toList());

		List<Course> courses = university.getCourses().stream().filter(c -> coursesNames.contains(c.getName()))
				.collect(Collectors.toList());
		Teacher teacher = new Teacher(firstName, lastName, gender, date, email, phone, address, courses);
		university.addTeacher(teacher);
	}

	private void removeTeacher() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter teacher id you want to remove:");
		int id = scanner.nextInt();
		List<Teacher> teachers = university.getTeachers();
		List<Teacher> teachersForRemoving = teachers.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		for (Teacher teacher : teachersForRemoving) {
			university.removeTeacher(teacher);
		}
	}

	private void addGroup() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter group name:");
		String name = scanner.next();
		Group group = new Group(name);
		university.addGroup(group);
	}

	private void removeGroup() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter group id you want to remove:");
		int id = scanner.nextInt();
		List<Group> groups = university.getGroups();
		List<Group> groupsForRemoving = groups.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		for (Group group : groupsForRemoving) {
			university.removeGroup(group);
		}
	}

	private void addAuditorium() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter auditorium name:");
		String name = scanner.next();
		System.out.println("Enter auditorium capacity:");
		int capacity = scanner.nextInt();
		Auditorium auditorium = new Auditorium(name, capacity);
		university.addAuditorium(auditorium);
	}

	private void removeAuditorium() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter auditorium id you want to remove:");
		int id = scanner.nextInt();
		List<Auditorium> auditoriums = university.getAuditoriums();
		List<Auditorium> auditoriumsForRemoving = auditoriums.stream().filter(s -> s.getId() == id)
				.collect(Collectors.toList());
		for (Auditorium auditorium : auditoriumsForRemoving) {
			university.removeAuditorium(auditorium);
		}
	}

	private void createLessonTime() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime endTime = LocalTime.parse("09:00");
		ArrayList<LessonTime> lessonsTime = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			lessonsTime.add(new LessonTime(i + 1, startTime.plusHours(i), endTime.plusHours(i)));
		}
		university.setLessonsTime(lessonsTime);
	}

	private void addLesson() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter course id:");
		int courseId = scanner.nextInt();
		List<Course> courses = university.getCourses();
		List<Course> courseForAdding = courses.stream().filter(c -> c.getId() == courseId).collect(Collectors.toList());
		Course course = courseForAdding.get(0);

		System.out.println("Enter teacher id:");
		int teacherId = scanner.nextInt();
		List<Teacher> teachers = university.getTeachers();
		List<Teacher> teacherForAdding = teachers.stream().filter(c -> c.getId() == teacherId)
				.collect(Collectors.toList());
		Teacher teacher = teacherForAdding.get(0);

		System.out.println("Enter groups id separated by commas( For example: 1,2,5 ):");
		String groupId = scanner.next();
		List<Integer> groupsId = Arrays.stream(groupId.split(",")).map(s -> Integer.parseInt(s))
				.collect(Collectors.toList());
		List<Group> groups = university.getGroups().stream().filter(g -> groupsId.contains(g.getId()))
				.collect(Collectors.toList());

		System.out.println("Enter day of lesson in format \"yyyy-MM-dd\":");
		String date = scanner.next();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate day = LocalDate.parse(date, formatter);
		System.out.println("Enter order number of lesson:");
		int order = scanner.nextInt();
		List<LessonTime> lessonTimes = university.getLessonsTime();
		List<LessonTime> timeForAdding = lessonTimes.stream().filter(t -> t.getOrderNumber() == order)
				.collect(Collectors.toList());
		LessonTime lessonTime = timeForAdding.get(0);

		System.out.println("Enter auditorium id:");
		int auditoriumId = scanner.nextInt();
		List<Auditorium> auditoriums = university.getAuditoriums();
		List<Auditorium> auditoriumForAdding = auditoriums.stream().filter(t -> t.getId() == auditoriumId)
				.collect(Collectors.toList());
		Auditorium auditorium = auditoriumForAdding.get(0);

		Lesson lesson = new Lesson(course, teacher, groups, day, lessonTime, auditorium);
	}
}
