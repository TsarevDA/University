package ru.tsar.university.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

	@GetMapping()
	public String getAll(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1) - 1;
		int pageSize = size.orElse(5);
		int startItem = currentPage * pageSize;
		
		List<Auditorium> auditoriums = auditoriumService.getAll();
		List<Auditorium> pageList;

		if (auditoriums.size() < startItem) {
			pageList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, auditoriums.size());
			pageList = auditoriums.subList(startItem, toIndex);
		}

		Page<Auditorium> auditoriumPage = new PageImpl<>(pageList,
				PageRequest.of(currentPage, pageSize, Sort.by(Sort.Direction.ASC, "capacity")), auditoriums.size());
		
		model.addAttribute("auditoriumsPage", auditoriumPage);

		int totalPages = auditoriumPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return ("auditorium/index");
	}

}