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

import ru.tsar.university.model.Student;
import ru.tsar.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

	private StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable int id, Model model) {

		model.addAttribute("student", studentService.getById(id));
		return ("student/show");
	}

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Student> studentPage = studentService.getAll(pageable);
		model.addAttribute("studentsPage", studentPage);
		return ("student/index");
	}

	@GetMapping("/new")
	public String setStudent() {
		return ("student/new");
	}

	@PostMapping("/create")
	public String createStudent(@ModelAttribute Student student) {
		studentService.create(student);
		return "redirect:/students";
	}

	@GetMapping("/update")
	public String updateStudent(@RequestParam int id, Model model) {
		model.addAttribute("student", studentService.getById(id));
		return ("student/update");
	}

	@GetMapping("/delete")
	public String delete(@RequestParam int id) {
		studentService.deleteById(id);
		return ("redirect:/students");
	}

	@PostMapping("/save")
	public String saveStudentUpdate(@ModelAttribute Student student) {
		studentService.update(student);
		return "redirect:/students";
	}
}
