package ru.tsar.university.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public String getAll(Model model,  Pageable pageable) {
		
		Page<Auditorium> auditoriumPage = auditoriumService.getAll(pageable);
		model.addAttribute("auditoriumsPage", auditoriumPage);
		return ("auditorium/index");
	}
}