package ru.tsar.university.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Teacher;
import ru.tsar.university.service.TeacherService;

@Component
public class TeacherConverter implements Converter<String, Teacher> {

	private TeacherService teacherService;

	public TeacherConverter(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@Override
	public Teacher convert(String id) {
		int parsedId = Integer.parseInt(id);
		return teacherService.getById(parsedId);
	}
}
