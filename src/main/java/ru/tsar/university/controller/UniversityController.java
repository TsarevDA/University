package ru.tsar.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UniversityController {

	@GetMapping()
	public String get() {
		return "university/index";
	}
}
