package ru.tsar.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;
	
	@GetMapping("/{id}")
	public String getById(
			@PathVariable("id") int id,
			Model model) {
		
		model.addAttribute("teacher", teacherService.getById(id));
		return ("teacher/show");
	}
	
	@GetMapping()
	public String getAll(Model model) {
		
		model.addAttribute("teachers", teacherService.getAll());
		return ("teacher/index");
	}	
	
	@GetMapping("/find")
	public String find(@RequestParam(value="id",required=false) Integer id,
			Model model) {
		if (id!=null) {
			model.addAttribute("teacher", teacherService.getById(id));
			return ("teacher/show");
		}
		return ("teacher/find");
	}
}
