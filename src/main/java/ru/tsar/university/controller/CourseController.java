package ru.tsar.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {
	@Autowired
	private CourseService courseService;
	
	@GetMapping("/{id}")
	public String getById(
			@PathVariable("id") int id,
			Model model) {
		
		model.addAttribute("course", courseService.getById(id));
		return ("course/show");
	}
	
	@GetMapping("/find")
	public String find(@RequestParam(value="id",required=false) Integer id,
			Model model) {
		if (id!=null) {
			model.addAttribute("course", courseService.getById(id));
			return ("course/show");
		}
		return ("course/find");
	}
	
	@GetMapping()
	public String getAll(Model model) {
		
		model.addAttribute("courses", courseService.getAll());
		return ("course/index");
	}	
}
