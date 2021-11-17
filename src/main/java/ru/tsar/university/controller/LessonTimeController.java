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

import ru.tsar.university.model.Group;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.service.LessonTimeService;

@Controller
@RequestMapping("/lessontimes")
public class LessonTimeController {

	private LessonTimeService lessonTimeService;
	
	public LessonTimeController(LessonTimeService lessonTimeService) {
		this.lessonTimeService = lessonTimeService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable int id, Model model) {

		model.addAttribute("lessonTime", lessonTimeService.getById(id));
		return ("lessonTime/show");
	}

	@GetMapping()
	public String getAll(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1) - 1;
		int pageSize = size.orElse(5);
		int startItem = currentPage * pageSize;

		List<LessonTime> lessonTimes = lessonTimeService.getAll();
		List<LessonTime> pageList;

		if (lessonTimes.size() < startItem) {
			pageList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, lessonTimes.size());
			pageList = lessonTimes.subList(startItem, toIndex);
		}

		Page<LessonTime> lessonTimesPage = new PageImpl<>(pageList, PageRequest.of(currentPage, pageSize),
				lessonTimes.size());

		model.addAttribute("lessonTimesPage", lessonTimesPage);

		int totalPages = lessonTimesPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		return ("lessonTime/index");
	}

}
