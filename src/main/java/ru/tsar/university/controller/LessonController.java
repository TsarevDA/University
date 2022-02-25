package ru.tsar.university.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;
import ru.tsar.university.service.AuditoriumService;
import ru.tsar.university.service.CourseService;
import ru.tsar.university.service.GroupService;
import ru.tsar.university.service.LessonService;
import ru.tsar.university.service.LessonTimeService;
import ru.tsar.university.service.TeacherService;

@Controller
@RequestMapping("/lessons")
public class LessonController {

	private static final Logger log = LoggerFactory.getLogger(LessonController.class);

	private LessonService lessonService;
	private TeacherService teacherService;
	private CourseService courseService;
	private GroupService groupService;
	private LessonTimeService lessonTimeService;
	private AuditoriumService auditoriumService;

	public LessonController(LessonService lessonService, CourseService courseService, TeacherService teacherService,
			GroupService groupService, LessonTimeService lessonTimeService, AuditoriumService auditoriumService) {
		this.lessonService = lessonService;
		this.courseService = courseService;
		this.teacherService = teacherService;
		this.groupService = groupService;
		this.lessonTimeService = lessonTimeService;
		this.auditoriumService = auditoriumService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable int id, Model model) {

		model.addAttribute("lesson", lessonService.getById(id));
		return "lesson/show";
	}

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Lesson> lessonPage = lessonService.getAll(pageable);
		model.addAttribute("lessonsPage", lessonPage);
		return "lesson/index";
	}

	@GetMapping("/groups")
	public String getGroupStudents(@RequestParam(value = "lessonId", required = false) Integer lessonId, Model model) {

		model.addAttribute("groups", groupService.getByLessonId(lessonId));
		return "group/indexNotPageable";
	}

	@GetMapping("/new")
	public String returnNewLesson(Model model) {
		model.addAttribute("lesson",
				Lesson.builder().course(Course.builder().build()).teacher(Teacher.builder().build())
						.auditorium(Auditorium.builder().build()).lessonTime(LessonTime.builder().build())
						.group(new ArrayList<Group>()).build());
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("teachers", teacherService.getAll());
		model.addAttribute("auditoriums", auditoriumService.getAll());
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("lessonTimes", lessonTimeService.getAll());
		return "lesson/new";
	}

	@PostMapping("/create")
	public String createLesson(@ModelAttribute Lesson lesson) {

		lesson.setCourse(courseService.getById(lesson.getCourse().getId()));
		lesson.setAuditorium(auditoriumService.getById(lesson.getAuditorium().getId()));
		lesson.setLessonTime(lessonTimeService.getById(lesson.getLessonTime().getId()));
		lesson.setTeacher(teacherService.getById(lesson.getTeacher().getId()));
		lessonService.create(lesson);
		return "redirect:/lessons";
	}

	@GetMapping("/update")
	public String updateLesson(@RequestParam int id, Model model) {
		Lesson lesson = lessonService.getById(id);
		model.addAttribute("lesson", lesson);
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("teachers", teacherService.getAll());
		model.addAttribute("auditoriums", auditoriumService.getAll());
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("lessonTimes", lessonTimeService.getAll());
		return "lesson/update";
	}

	@GetMapping("/delete")
	public String deleteLesson(@RequestParam int id) {
		try {
			lessonService.deleteById(id);
		} catch (EntityNotFoundException e) {
			log.warn("Lesson not found by id = {}", id);

		}
		return "redirect:/lessons";
	}

	@PostMapping("/save")
	public String saveLessonUpdate(@ModelAttribute Lesson lesson) {
		lessonService.update(lesson);
		return "redirect:/lessons";
	}
}
