package ru.tsar.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.service.GroupService;
import ru.tsar.university.service.LessonService;

@Controller
@RequestMapping("/lessons")
public class LessonController {
	
	@Autowired
	private LessonService lessonService;
	@Autowired
	private GroupService groupService;
	
	@GetMapping("/{id}")
	public String getById(
			@PathVariable("id") int id,
			Model model) {
		
		model.addAttribute("lesson", lessonService.getById(id));
		return ("lesson/show");
	}
	
	@GetMapping()
	public String getAll(Model model) {
		
		model.addAttribute("lessons", lessonService.getAll());
		return ("lesson/index");
	}	
	
	@GetMapping("/groups")
	public String getGroupStudents(@RequestParam(value="lessonId",required=false) Integer lessonId,Model model) {
		
		model.addAttribute("groups", groupService.getByLessonId(lessonId));
		return ("group/index");
	}	
	
	
	@GetMapping("/find")
	public String find(@RequestParam(value="id",required=false) Integer id,
			Model model) {
		if (id!=null) {
			model.addAttribute("lesson", lessonService.getById(id));
			return ("lesson/show");
		}
		return ("lesson/find");
	}
}
