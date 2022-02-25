package ru.tsar.university.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Student;
import ru.tsar.university.service.StudentService;

@Component
public class StudentConverter implements Converter<String, Student> {

	private StudentService studentService;

	public StudentConverter(StudentService studentService) {
		this.studentService = studentService;
	}

	@Override
	public Student convert(String id) {
		int parsedId = Integer.parseInt(id);
		return studentService.getById(parsedId);
	}
}
