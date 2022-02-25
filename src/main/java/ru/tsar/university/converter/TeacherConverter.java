package ru.tsar.university.converter;

import org.springframework.core.convert.converter.Converter;

import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Teacher;
import ru.tsar.university.service.TeacherService;

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
