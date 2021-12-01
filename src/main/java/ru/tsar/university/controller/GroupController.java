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

import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;
import ru.tsar.university.service.GroupService;
import ru.tsar.university.service.StudentService;

@Controller
@RequestMapping("/groups")
public class GroupController {

	private GroupService groupService;
	private StudentService studentService;

	GroupController(GroupService groupService, StudentService studentService) {
		this.groupService = groupService;
		this.studentService = studentService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable("id") int id, Model model) {

		model.addAttribute("group", groupService.getById(id));
		return ("group/show");
	}

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Group> groupPage = groupService.getAll(pageable);
		model.addAttribute("groupsPage", groupPage);
		return ("group/index");
	}

	@GetMapping("/students")
	public String getGroupStudents(@RequestParam(value = "groupId", required = false) Integer groupId, Model model, Pageable pageable) {

		model.addAttribute("studentsPage", studentService.getByGroupId(groupId, pageable));
		return ("student/index");
	}
}