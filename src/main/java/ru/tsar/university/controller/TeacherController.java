package ru.tsar.university.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Teacher;
import ru.tsar.university.service.CourseService;
import ru.tsar.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	private static final Logger log = LoggerFactory.getLogger(TeacherController.class);

	private TeacherService teacherService;
	private CourseService courseService;

	public TeacherController(TeacherService teacherService, CourseService courseService) {
		this.teacherService = teacherService;
		this.courseService = courseService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable int id, Model model) {

		model.addAttribute("teacher", teacherService.getById(id));
		return "teacher/show";
	}

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Teacher> teacherPage = teacherService.getAll(pageable);
		model.addAttribute("teachersPage", teacherPage);
		return "teacher/index";
	}

	@GetMapping("/new")
	public String returnNewTeacher(Model model) {
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("teacher", Teacher.builder().courses(new ArrayList<Course>()).build());
		return "teacher/new";
	}

	@PostMapping("/create")
	public String createTeacher(@ModelAttribute Teacher teacher) {
		teacherService.create(teacher);
		return "redirect:/teachers";
	}

	@GetMapping("/update")
	public String updateTeacher(@RequestParam int id, Model model) {
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("teacher", teacherService.getById(id));
		return "teacher/update";
	}

	@GetMapping("/delete")
	public String deleteTeacher(@RequestParam int id) {
		try {
			teacherService.deleteById(id);
		} catch (EntityNotFoundException e) {
			log.warn("Teacher not found by id = {}", id);

		}
		return "redirect:/teachers";
	}

	@PostMapping("/save")
	public String saveUpdatedTeacher(@ModelAttribute Teacher teacher) {
		teacherService.update(teacher);
		return "redirect:/teachers";
	}
}