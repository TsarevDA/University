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
	public String getById(
			@PathVariable("id") int id,
			Model model) {
		
		model.addAttribute("group", groupService.getById(id));
		return ("group/show");
	}
	
	@GetMapping()
	public String getAll(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		
		int currentPage = page.orElse(1) - 1;
		int pageSize = size.orElse(5);
		int startItem = currentPage * pageSize;
		
		List<Group> groups = groupService.getAll();
		List<Group> pageList;

		if (groups.size() < startItem) {
			pageList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, groups.size());
			pageList = groups.subList(startItem, toIndex);
		}

		Page<Group> groupPage = new PageImpl<>(pageList,
				PageRequest.of(currentPage, pageSize), groups.size());
		
		model.addAttribute("groupsPage", groupPage);

		int totalPages = groupPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
	
		return ("group/index");
	}	
	
	@GetMapping("/students")
	public String getGroupStudents(@RequestParam(value="groupId",required=false) Integer groupId,Model model) {
		
		model.addAttribute("students", studentService.getByGroupId(groupId));
		return ("student/index");
	}	
}