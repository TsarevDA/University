package ru.tsar.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;

import ru.tsar.dao.AuditoriumDao;
import ru.tsar.dao.CourseDao;
import ru.tsar.dao.GroupDao;
import ru.tsar.dao.LessonDao;
import ru.tsar.dao.LessonTimeDao;
import ru.tsar.dao.StudentDao;
import ru.tsar.dao.TeacherDao;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;
import ru.tsar.university.model.Gender;

public class ConsoleInterface {

	final private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private University university;
	private JdbcTemplate jdbcTemplate;

	public ConsoleInterface(University university, JdbcTemplate jdbcTemplate) {
		this.university = university;
		this.jdbcTemplate = jdbcTemplate;
	}

	public void startMenu() {

		Scanner scanner = new Scanner(System.in);
		String line = "";

		while (!line.equals("q")) {
			System.out.println("for exit press 'q', for help press 'h'");
			line = scanner.next();

			if (line.equals("h")) {
				// System.out.println(helpMenu());
			}
			if (line.equals("as")) {
				addStudent();
			}
			if (line.equals("ds")) {
				deleteStudent();
			}
			if (line.equals("ac")) {
				addCourse();
			}
			if (line.equals("dc")) {
				deleteCourse();
			}
			if (line.equals("ag")) {
				addGroup();
			}
			if (line.equals("dc")) {
				deleteGroup();
			}
			if (line.equals("at")) {
				addTeacher();
			}
			if (line.equals("dt")) {
				deleteTeacher();
			}
			if (line.equals("al")) {
				addLesson();
			}
			if (line.equals("dl")) {
				deleteLesson();
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
		Gender gender = Gender.valueOf(stringGender.toUpperCase());
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
		StudentDao studentDao = new StudentDao(jdbcTemplate);
		studentDao.addStudent(student);

		university.addStudent(student);
	}

	private void deleteStudent() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter student id you want to remove:");
		int id = scanner.nextInt();
		List<Student> students = university.getStudents();
		List<Student> studentsForRemoving = students.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		StudentDao studentDao = new StudentDao(jdbcTemplate);

		for (Student student : studentsForRemoving) {
			studentDao.deleteStudent(student.getId());
			university.deleteStudent(student);
		}
	}

	private void addCourse() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter course name:");
		String name = scanner.next();
		System.out.println("Enter course description:");
		String description = scanner.next();
		Course course = new Course(name, description);
		CourseDao courseDao = new CourseDao(jdbcTemplate);
		courseDao.addCourse(course);
		university.addCourse(course);
	}

	private void deleteCourse() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter course id you want to remove:");
		int id = scanner.nextInt();
		List<Course> courses = university.getCourses();
		List<Course> coursesForRemoving = courses.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		CourseDao courseDao = new CourseDao(jdbcTemplate);
		for (Course course : coursesForRemoving) {
			university.deleteCourse(course);
			courseDao.deleteCourse(course.getId());
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
		Gender gender = Gender.valueOf(stringGender.toUpperCase());
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
		TeacherDao teacherDao = new TeacherDao(jdbcTemplate);
		teacherDao.addTeacher(teacher);
		university.addTeacher(teacher);
	}

	private void deleteTeacher() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter teacher id you want to remove:");
		int id = scanner.nextInt();
		List<Teacher> teachers = university.getTeachers();
		List<Teacher> teachersForRemoving = teachers.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		TeacherDao teacherDao = new TeacherDao(jdbcTemplate);
		for (Teacher teacher : teachersForRemoving) {
			university.deleteTeacher(teacher);
			teacherDao.deleteTeacher(teacher.getId());
		}
	}

	private void addGroup() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter group name:");
		String name = scanner.next();
		Group group = new Group(name);
		GroupDao groupDao = new GroupDao(jdbcTemplate);
		groupDao.addGroup(group);
		university.addGroup(group);
	}

	private void deleteGroup() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter group id you want to remove:");
		int id = scanner.nextInt();
		List<Group> groups = university.getGroups();
		List<Group> groupsForRemoving = groups.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		GroupDao groupDao = new GroupDao(jdbcTemplate);
		for (Group group : groupsForRemoving) {
			university.deleteGroup(group);
			groupDao.deleteGroup(group.getId());
		}
	}

	private void addAuditorium() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter auditorium name:");
		String name = scanner.next();
		System.out.println("Enter auditorium capacity:");
		int capacity = scanner.nextInt();
		Auditorium auditorium = new Auditorium(name, capacity);
		AuditoriumDao auditoriumDao = new AuditoriumDao(jdbcTemplate);
		auditoriumDao.addAuditorium(auditorium);
		university.addAuditorium(auditorium);
	}

	private void deleteAuditorium() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter auditorium id you want to remove:");
		int id = scanner.nextInt();
		List<Auditorium> auditoriums = university.getAuditoriums();
		List<Auditorium> auditoriumsForRemoving = auditoriums.stream().filter(s -> s.getId() == id)
				.collect(Collectors.toList());
		AuditoriumDao auditoriumDao = new AuditoriumDao(jdbcTemplate);
		for (Auditorium auditorium : auditoriumsForRemoving) {
			university.deleteAuditorium(auditorium);
			auditoriumDao.deleteAuditorium(auditorium.getId());
		}
	}

	private void createLessonTime() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime endTime = LocalTime.parse("09:00");
		ArrayList<LessonTime> lessonsTime = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			lessonsTime.add(new LessonTime(i + 1, startTime.plusHours(i), endTime.plusHours(i)));
		}
		LessonTimeDao lessonTimeDao = new LessonTimeDao(jdbcTemplate);
		lessonsTime.stream().forEach(lt -> lessonTimeDao.addLessonTime(lt));
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

		LessonDao lessonDao = new LessonDao(jdbcTemplate);
		lessonDao.addLesson(lesson);

	}

	private void deleteLesson() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter lesson id you want to remove:");
		int id = scanner.nextInt();
		List<Lesson> lessons = university.getLessons();
		List<Lesson> lessonForRemoving = lessons.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		LessonDao lessonDao = new LessonDao(jdbcTemplate);

		for (Lesson lesson : lessonForRemoving) {
			university.deleteLesson(lesson);
			lessonDao.deleteLesson(lesson.getId());
		}

	}

}
