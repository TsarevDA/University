package ru.tsar.university.controller;

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

import ru.tsar.university.service.AuditoriumService;
import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.model.Auditorium;

@Controller
@RequestMapping("/auditoriums")
public class AuditoriumController {

	private static final Logger log = LoggerFactory.getLogger(AuditoriumController.class);

	private AuditoriumService auditoriumService;

	public AuditoriumController(AuditoriumService auditoriumService) {
		this.auditoriumService = auditoriumService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable int id, Model model) {

		model.addAttribute("auditorium", auditoriumService.getById(id));
		return "auditorium/show";
	}

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Auditorium> auditoriumPage = auditoriumService.getAll(pageable);
		model.addAttribute("auditoriumsPage", auditoriumPage);
		return "auditorium/index";
	}

	@GetMapping("/new")
	public String returnNewAuditorium() {
		return "auditorium/new";
	}

	@GetMapping("/update")
	public String updateAuditorium(@RequestParam int id, Model model) {
		model.addAttribute("auditorium", auditoriumService.getById(id));
		return "auditorium/update";
	}

	@GetMapping("/delete")
	public String deleteAuditorium(@RequestParam int id) {
		try {
			auditoriumService.deleteById(id);
		} catch (EntityNotFoundException e) {
			log.warn("Auditorium not found by id = {}", id);

		}
		return "redirect:/auditoriums";
	}

	@PostMapping("/save")
	public String saveUpdatedAuditorium(@ModelAttribute Auditorium auditorium) {
		auditoriumService.update(auditorium);
		return "redirect:/auditoriums";
	}

	@PostMapping("/create")
	public String createAuditorium(@ModelAttribute Auditorium auditorium) {
		auditoriumService.create(auditorium);
		return "redirect:/auditoriums";
	}
}