package ru.tsar.university.controller;

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

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<LessonTime> lessonTimesPage = lessonTimeService.getAll(pageable);
		model.addAttribute("lessonTimesPage", lessonTimesPage);
		return ("lessonTime/index");
	}

	@GetMapping("/new")
	public String setLessonTime() {
		return ("lessonTime/new");
	}

	@PostMapping("/create")
	public String createLessonTime(@ModelAttribute LessonTime lessonTime) {
		lessonTimeService.create(lessonTime);
		return "redirect:/lessontimes";
	}

	@GetMapping("/update")
	public String updateLessonTime(@RequestParam int id, Model model) {
		model.addAttribute("lessonTime", lessonTimeService.getById(id));
		return ("lessonTime/update");
	}

	@GetMapping("/delete")
	public String deleteLessonTime(@RequestParam int id) {
		lessonTimeService.deleteById(id);
		return ("redirect:/lessontimes");
	}

	@PostMapping("/save")
	public String saveLessonTimeUpdate(@ModelAttribute LessonTime lessonTime) {
		lessonTimeService.update(lessonTime);
		return "redirect:/lessontimes";
	}

}
