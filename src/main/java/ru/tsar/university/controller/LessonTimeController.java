package ru.tsar.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.service.LessonTimeService;

@Controller
@RequestMapping("/lessontimes")
public class LessonTimeController {

	@Autowired
	private LessonTimeService lessonTimeService;
	
	@GetMapping("/{id}")
	public String getById(
			@PathVariable("id") int id,
			Model model) {
		
		model.addAttribute("lessonTime", lessonTimeService.getById(id));
		return ("lessonTime/show");
	}
	
	@GetMapping()
	public String getAll(Model model) {
		
		model.addAttribute("lessonTimes", lessonTimeService.getAll());
		return ("lessonTime/index");
	}	
	
	@GetMapping("/find")
	public String find(@RequestParam(value="id",required=false) Integer id,
			Model model) {
		if (id!=null) {
			model.addAttribute("lessonTime", lessonTimeService.getById(id));
			return ("lessonTime/show");
		}
		return ("lessonTime/find");
	}
}
