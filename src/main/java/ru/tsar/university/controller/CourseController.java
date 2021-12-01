package ru.tsar.university.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
