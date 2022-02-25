package ru.tsar.university.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;
import ru.tsar.university.service.GroupService;
import ru.tsar.university.service.StudentService;

@Controller
@RequestMapping("/groups")
public class GroupController {

	private static final Logger log = LoggerFactory.getLogger(GroupController.class);

	private GroupService groupService;
	private StudentService studentService;

	GroupController(GroupService groupService, StudentService studentService) {
		this.groupService = groupService;
		this.studentService = studentService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable("id") int id, Model model) {

		model.addAttribute("group", groupService.getById(id));
		return "group/show";
	}

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Group> groupPage = groupService.getAll(pageable);
		model.addAttribute("groupsPage", groupPage);
		return "group/index";
	}

	@GetMapping("/students")
	public String getGroupStudents(@RequestParam(value = "groupId", required = false) Integer groupId, Model model,
			Pageable pageable) {

		model.addAttribute("studentsPage", studentService.getByGroupId(groupId, pageable));
		return "student/index";
	}

	@GetMapping("/new")
	public String returnNewGroup(Model model) {
		model.addAttribute("students", studentService.getAll());
		model.addAttribute("group", Group.builder().students(new ArrayList<Student>()).build());
		return "group/new";
	}

	@PostMapping("/create")
	public String createGroup(@ModelAttribute Group group) {
		groupService.create(group);
		return "redirect:/groups";
	}

	@GetMapping("/update")
	public String updateGroup(@RequestParam int id, Model model) {
		Group group = groupService.getById(id);
		model.addAttribute("group", group);
		model.addAttribute("students", studentService.getAll());
		return "group/update";
	}

	@GetMapping("/delete")
	public String deleteGroup(@RequestParam int id) {
		try {
			groupService.deleteById(id);
		} catch (EntityNotFoundException e) {
			log.warn("Group not found by id = {}", id);

		}
		return "redirect:/groups";
	}

	@PostMapping("/save")
	public String saveGroupUpdate(@ModelAttribute Group group) {
		groupService.update(group);
		return "redirect:/groups";
	}
}