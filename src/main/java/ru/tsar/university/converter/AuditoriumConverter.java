package ru.tsar.university.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.service.AuditoriumService;

public class AuditoriumConverter implements Converter<String, Auditorium> {

	private AuditoriumService auditoriumService;

	public AuditoriumConverter(AuditoriumService auditoriumService) {
		this.auditoriumService = auditoriumService;
	}

	@Override
	public Auditorium convert(String id) {
		int parsedId = Integer.parseInt(id);
		return auditoriumService.getById(parsedId);
	}
}
