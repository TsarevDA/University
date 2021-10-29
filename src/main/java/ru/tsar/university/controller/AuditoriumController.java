package ru.tsar.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.service.AuditoriumService;

@Controller
@RequestMapping("/auditoriums")
public class AuditoriumController {
	@Autowired
	private AuditoriumService auditoriumService;
	
	@GetMapping("/{id}")
	public String getById(
			@PathVariable("id") int id,
			Model model) {
		
		model.addAttribute("auditorium", auditoriumService.getById(id));
		return ("auditorium/show");
	}
	
	@GetMapping("/find")
	public String find(@RequestParam(value="id",required=false) Integer id,
			Model model) {
		if (id!=null) {
			model.addAttribute("auditorium", auditoriumService.getById(id));
			return ("auditorium/show");
		}
		return ("auditorium/find");
	}
	
	@GetMapping()
	public String getAll(Model model) {
		model.addAttribute("auditoriums", auditoriumService.getAll());
		return ("auditorium/index");	
	}	
	
}









