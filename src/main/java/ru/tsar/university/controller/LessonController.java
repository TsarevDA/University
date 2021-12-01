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

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Lesson> lessonPage = lessonService.getAll(pageable);
		model.addAttribute("lessonsPage", lessonPage);
		return ("lesson/index");
	}

	@GetMapping("/groups")
	public String getGroupStudents(@RequestParam(value = "lessonId", required = false) Integer lessonId, Model model) {

		model.addAttribute("groups", groupService.getByLessonId(lessonId));
		return ("group/index");
	}

}
