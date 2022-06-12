package ru.tsar.university.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Group;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.service.GroupService;
import ru.tsar.university.service.LessonTimeService;

@Component
public class LessonTimeConverter implements Converter<String, LessonTime> {

	private LessonTimeService lessonTimeService;

	public LessonTimeConverter(LessonTimeService lessonTimeService) {
		this.lessonTimeService = lessonTimeService;
	}

	@Override
	public LessonTime convert(String id) {
		int parsedId = Integer.parseInt(id);
		return lessonTimeService.getById(parsedId);
	}

}
