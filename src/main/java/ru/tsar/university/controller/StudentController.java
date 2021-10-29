package ru.tsar.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@GetMapping("/{id}")
	public String getById(
			@PathVariable("id") int id,
			Model model) {
		
		model.addAttribute("student", studentService.getById(id));
		return ("student/show");
	}
	
	@GetMapping()
	public String getAll(Model model) {
		try {
		model.addAttribute("students", studentService.getAll());
		} catch (NullPointerException e) {
			System.out.println("DETECTED NULL!!!");
		}
		return ("student/index");
		
	}	
	
	@GetMapping("/find")
	public String find(@RequestParam(value="id",required=false) Integer id,
			Model model) {
		if (id!=null) {
			model.addAttribute("student", studentService.getById(id));
			return ("student/show");
		}
		return ("student/find");
	}
}
