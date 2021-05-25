package ru.tsar.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.tsar.university.service.AuditoriumService;
import ru.tsar.university.service.CourseService;
import ru.tsar.university.service.GroupService;
import ru.tsar.university.service.LessonService;
import ru.tsar.university.service.LessonTimeService;
import ru.tsar.university.service.StudentService;
import ru.tsar.university.service.TeacherService;
import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.exceptions.AuditoriumFreeException;
import ru.tsar.university.exceptions.AuditroiumExistException;
import ru.tsar.university.exceptions.CapacityEnoughException;
import ru.tsar.university.exceptions.CourseExistException;
import ru.tsar.university.exceptions.DayOffException;
import ru.tsar.university.exceptions.GroupExistException;
import ru.tsar.university.exceptions.GroupFreeException;
import ru.tsar.university.exceptions.LessonExistException;
import ru.tsar.university.exceptions.StudentExistException;
import ru.tsar.university.exceptions.TeacherCompetentException;
import ru.tsar.university.exceptions.TeacherExistException;
import ru.tsar.university.exceptions.TeacherFreeException;
import ru.tsar.university.exceptions.TimeCorrectException;
import ru.tsar.university.exceptions.UniqueNameException;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;
import ru.tsar.university.model.Gender;

@Component
public class ConsoleInterface {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConsoleInterface.class);
	
	final private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	@Autowired
	private University university;

	@Autowired
	private StudentService studentService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private AuditoriumService auditoriumService;
	@Autowired
	private LessonTimeService lessonTimeService;
	@Autowired
	private LessonService lessonService;

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

		Student student = Student.builder().firstName(firstName).lastName(lastName).gender(gender).birthDate(date)
				.email(email).phone(phone).address(address).build();
		studentService.create(student);

		university.addStudent(student);
	}

	private void deleteStudent() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter student id you want to remove:");
		int id = scanner.nextInt();
		List<Student> students = university.getStudents();
		List<Student> studentsForRemoving = students.stream().filter(s -> s.getId() == id).collect(Collectors.toList());

		for (Student student : studentsForRemoving) {
			try {
				studentService.deleteById(student.getId());
			} catch (StudentExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			university.deleteStudent(student);
		}
	}

	private void addCourse() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter course name:");
		String name = scanner.next();
		System.out.println("Enter course description:");
		String description = scanner.next();
		Course course = Course.builder().name(name).description(description).build();

		try {
			courseService.create(course);
		} catch (UniqueNameException e) {
			LOG.warn(e.getMessage());
		}
		university.addCourse(course);
	}

	private void deleteCourse() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter course id you want to remove:");
		int id = scanner.nextInt();
		List<Course> courses = university.getCourses();
		List<Course> coursesForRemoving = courses.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		for (Course course : coursesForRemoving) {
			university.deleteCourse(course);
			try {
				courseService.deleteById(course.getId());
			} catch (CourseExistException e) {
				LOG.warn(e.getMessage());
			}
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
		Teacher teacher = Teacher.builder().firstName(firstName).lastName(lastName).gender(gender).birthDate(date)
				.email(email).phone(phone).address(address).courses(courses).build();

		teacherService.create(teacher);
		university.addTeacher(teacher);
	}

	private void deleteTeacher() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter teacher id you want to remove:");
		int id = scanner.nextInt();
		List<Teacher> teachers = university.getTeachers();
		List<Teacher> teachersForRemoving = teachers.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		for (Teacher teacher : teachersForRemoving) {
			university.deleteTeacher(teacher);
			try {
				teacherService.deleteById(teacher.getId());
			} catch (TeacherExistException e) {
				LOG.warn(e.getMessage());
			}
		}
	}

	private void addGroup() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter group name:");
		String name = scanner.next();
		// Group group = new Group(name);
		Group group = Group.builder().name(name).build();
		try {
			groupService.create(group);
		} catch (UniqueNameException e) {
			LOG.warn(e.getMessage());
		}
		university.addGroup(group);
	}

	private void deleteGroup() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter group id you want to remove:");
		int id = scanner.nextInt();
		List<Group> groups = university.getGroups();
		List<Group> groupsForRemoving = groups.stream().filter(s -> s.getId() == id).collect(Collectors.toList());
		for (Group group : groupsForRemoving) {
			university.deleteGroup(group);
			try {
				groupService.deleteById(group.getId());
			} catch (GroupExistException e) {
				LOG.warn(e.getMessage());
			}
		}
	}

	private void addAuditorium() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter auditorium name:");
		String name = scanner.next();
		System.out.println("Enter auditorium capacity:");
		int capacity = scanner.nextInt();

		Auditorium auditorium = Auditorium.builder().name(name).capacity(capacity).build();
		try {
			auditoriumService.create(auditorium);
		} catch (UniqueNameException e) {
			LOG.warn(e.getMessage());
		}
		university.addAuditorium(auditorium);
	}

	private void deleteAuditorium() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter auditorium id you want to remove:");
		int id = scanner.nextInt();
		List<Auditorium> auditoriums = university.getAuditoriums();
		List<Auditorium> auditoriumsForRemoving = auditoriums.stream().filter(s -> s.getId() == id)
				.collect(Collectors.toList());
		for (Auditorium auditorium : auditoriumsForRemoving) {
			university.deleteAuditorium(auditorium);
			try {
				auditoriumService.deleteById(auditorium.getId());
			} catch (AuditroiumExistException e) {
				LOG.warn(e.getMessage());
			}
		}
	}

	private void createLessonTime() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime endTime = LocalTime.parse("09:00");
		ArrayList<LessonTime> lessonsTime = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			lessonsTime.add(LessonTime.builder().orderNumber(i + 1).startTime(startTime.plusHours(i))
					.endTime(endTime.plusHours(i)).build());
		}
		lessonsTime.stream().forEach(lt -> {
			try {
				lessonTimeService.create(lt);
			} catch (TimeCorrectException e) {
				LOG.warn(e.getMessage());
			}
		});
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

		Lesson lesson = Lesson.builder().course(course).teacher(teacher).group(groups).day(day).time(lessonTime)
				.auditorium(auditorium).build();

		try {
			lessonService.create(lesson);
		} catch (CapacityEnoughException | TeacherCompetentException | DayOffException | AuditoriumFreeException
				| TeacherFreeException | GroupFreeException e) {
			LOG.warn(e.getMessage());
		}

	}

	private void deleteLesson() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter lesson id you want to remove:");
		int id = scanner.nextInt();
		List<Lesson> lessons = university.getLessons();
		List<Lesson> lessonForRemoving = lessons.stream().filter(s -> s.getId() == id).collect(Collectors.toList());

		for (Lesson lesson : lessonForRemoving) {
			university.deleteLesson(lesson);
			try {
				lessonService.deleteById(lesson.getId());
			} catch (LessonExistException e) {
				LOG.warn(e.getMessage());
			}
		}

	}

}
