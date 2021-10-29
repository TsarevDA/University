package ru.tsar.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.service.GroupService;
import ru.tsar.university.service.StudentService;

@Controller
@RequestMapping("/groups")
public class GroupController {
	@Autowired
	private GroupService groupService;
	@Autowired
	private StudentService studentService;
	
	@GetMapping("/{id}")
	public String getById(
			@PathVariable("id") int id,
			Model model) {
		
		model.addAttribute("group", groupService.getById(id));
		return ("group/show");
	}
	
	@GetMapping()
	public String getAll(Model model) {
		
		model.addAttribute("groups", groupService.getAll());
		return ("group/index");
	}	
	
	@GetMapping("/students")
	public String getGroupStudents(@RequestParam(value="groupId",required=false) Integer groupId,Model model) {
		
		model.addAttribute("students", studentService.getByGroupId(groupId));
		return ("student/index");
	}	
		
	@GetMapping("/find")
	public String find(@RequestParam(value="id",required=false) Integer id,
			Model model) {
		if (id!=null) {
			model.addAttribute("group", groupService.getById(id));
			return ("group/show");
		}
		return ("group/find");
	}
	
}