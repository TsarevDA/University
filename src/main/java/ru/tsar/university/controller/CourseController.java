package ru.tsar.university.controller;

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

import ru.tsar.university.model.Course;
import ru.tsar.university.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {

	private CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable int id, Model model) {

		model.addAttribute("course", courseService.getById(id));
		return ("course/show");
	}

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Course> coursePage = courseService.getAll(pageable);
		model.addAttribute("coursesPage", coursePage);
		return ("course/index");
	}

	@GetMapping("/new")
	public String setCourse() {
		return ("course/new");
	}

	@PostMapping("/create")
	public String createCourse(@ModelAttribute Course course) {
		courseService.create(course);
		return "redirect:/courses";
	}

	@GetMapping("/update")
	public String updateCourse(@RequestParam int id, Model model) {
		model.addAttribute("course", courseService.getById(id));
		return ("course/update");
	}

	@GetMapping("/delete")
	public String deleteCourse(@RequestParam int id) {
		courseService.deleteById(id);
		return ("redirect:/courses");
	}

	@PostMapping("/save")
	public String saveCourseUpdate(@ModelAttribute Course course) {
		courseService.update(course);
		return "redirect:/courses";
	}
}
