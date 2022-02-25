package ru.tsar.university.converter;

import org.springframework.core.convert.converter.Converter;

import ru.tsar.university.model.Course;
import ru.tsar.university.service.CourseService;

public class CourseConverter implements Converter<String, Course> {

	private CourseService courseService;

	public CourseConverter(CourseService courseService) {
		this.courseService = courseService;
	}

	@Override
	public Course convert(String id) {
		int parsedId = Integer.parseInt(id);
		return courseService.getById(parsedId);
	}
}
