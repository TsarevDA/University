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

import ru.tsar.university.service.AuditoriumService;
import ru.tsar.university.model.Auditorium;

@Controller
@RequestMapping("/auditoriums")
public class AuditoriumController {

	private AuditoriumService auditoriumService;

	public AuditoriumController(AuditoriumService auditoriumService) {
		this.auditoriumService = auditoriumService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable int id, Model model) {

		model.addAttribute("auditorium", auditoriumService.getById(id));
		return ("auditorium/show");
	}

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Auditorium> auditoriumPage = auditoriumService.getAll(pageable);
		model.addAttribute("auditoriumsPage", auditoriumPage);
		return ("auditorium/index");
	}

	@GetMapping("/new")
	public String setAuditorium() {
		return ("auditorium/new");
	}

	@GetMapping("/update")
	public String update(@RequestParam int id, Model model) {
		model.addAttribute("auditorium", auditoriumService.getById(id));
		return ("auditorium/update");
	}

	@GetMapping("/delete")
	public String delete(@RequestParam int id) {
		auditoriumService.deleteById(id);
		return ("redirect:/auditoriums");
	}

	@PostMapping("/save")
	public String save(@ModelAttribute Auditorium auditorium) {
		auditoriumService.update(auditorium);
		return "redirect:/auditoriums";
	}

	@PostMapping("/create")
	public String create(@ModelAttribute Auditorium auditorium) {
		auditoriumService.create(auditorium);
		return "redirect:/auditoriums";
	}
}