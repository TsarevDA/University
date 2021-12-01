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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.model.Group;
import ru.tsar.university.model.Teacher;
import ru.tsar.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	private TeacherService teacherService;

	public TeacherController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable int id, Model model) {

		model.addAttribute("teacher", teacherService.getById(id));
		return ("teacher/show");
	}

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Teacher> teacherPage = teacherService.getAll(pageable);
		model.addAttribute("teachersPage", teacherPage);
		return ("teacher/index");
	}
}
