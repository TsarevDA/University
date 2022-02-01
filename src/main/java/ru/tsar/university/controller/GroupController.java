package ru.tsar.university.controller;

import java.util.ArrayList;
import java.util.List;

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

import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;
import ru.tsar.university.service.GroupService;
import ru.tsar.university.service.StudentService;

@Controller
@RequestMapping("/groups")
public class GroupController {

	private GroupService groupService;
	private StudentService studentService;
	private List<Student> studentList = new ArrayList<>();

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
	public String getGroupStudents(@RequestParam(value = "groupId", required = false) Integer groupId, Model model,
			Pageable pageable) {

		model.addAttribute("studentsPage", studentService.getByGroupId(groupId, pageable));
		return ("student/index");
	}

	@GetMapping("/new")
	public String setGroup(Model model) {
		model.addAttribute("students", studentService.getAll());
		model.addAttribute("studentList", studentList);
		return ("group/new");
	}

	@PostMapping("/addStudent")
	public String addStudentToGroup(@RequestParam("studentId") int studentId, @RequestParam("groupId") int groupId) {
		Student student = studentService.getById(studentId);
		if (!studentList.contains(student)) {
			studentList.add(student);
		}
		if (groupId > 0)
			return ("redirect:/groups/update" + "?" + "id=" + groupId);
		return ("redirect:/groups/new");
	}

	@PostMapping("/removeStudent")
	public String removeStudentFromGroup(@RequestParam("studentId") int studentId,
			@RequestParam("groupId") int groupId) {
		studentList.remove(studentService.getById(studentId));
		if (groupId > 0)
			return ("redirect:/groups/update" + "?" + "id=" + groupId);
		return ("redirect:/groups/new");
	}

	@PostMapping("/create")
	public String createGroup(@ModelAttribute Group group) {
		group.setStudents(studentList);
		groupService.create(group);
		studentList.clear();
		return "redirect:/groups";
	}

	@GetMapping("/update")
	public String updateGroup(@RequestParam int id, Model model) {
		Group group = groupService.getById(id);
		model.addAttribute("group", group);
		if (studentList.size() == 0) {
			studentList = group.getStudents();
		}
		model.addAttribute("students", studentService.getAll());
		model.addAttribute("studentList", studentList);
		return ("group/update");
	}

	@GetMapping("/delete")
	public String deleteGroup(@RequestParam int id) {
		groupService.deleteById(id);
		return ("redirect:/groups");
	}

	@PostMapping("/save")
	public String saveGroupUpdate(@ModelAttribute Group group) {
		group.setStudents(studentList);
		groupService.update(group);
		studentList.clear();
		return "redirect:/groups";
	}
}