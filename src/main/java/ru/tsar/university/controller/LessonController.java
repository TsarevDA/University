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


import ru.tsar.university.model.Lesson;
import ru.tsar.university.service.GroupService;
import ru.tsar.university.service.LessonService;

@Controller
@RequestMapping("/lessons")
public class LessonController {

	
	private LessonService lessonService;
	private GroupService groupService;
	
	public LessonController(LessonService lessonService, GroupService groupService) {
		this.lessonService = lessonService;
		this.groupService = groupService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable int id, Model model) {

		model.addAttribute("lesson", lessonService.getById(id));
		return ("lesson/show");
	}

	@GetMapping()
	public String getAll(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1) - 1;
		int pageSize = size.orElse(5);
		int startItem = currentPage * pageSize;

		List<Lesson> lessons = lessonService.getAll();
		List<Lesson> pageList;

		if (lessons.size() < startItem) {
			pageList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, lessons.size());
			pageList = lessons.subList(startItem, toIndex);
		}

		Page<Lesson> lessonPage = new PageImpl<>(pageList, PageRequest.of(currentPage, pageSize), lessons.size());

		model.addAttribute("lessonsPage", lessonPage);

		int totalPages = lessonPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return ("lesson/index");
	}

	@GetMapping("/groups")
	public String getGroupStudents(@RequestParam(value = "lessonId", required = false) Integer lessonId, Model model) {

		model.addAttribute("groups", groupService.getByLessonId(lessonId));
		return ("group/index");
	}

}
