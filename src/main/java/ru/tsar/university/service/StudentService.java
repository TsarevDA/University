package ru.tsar.university.service;

import org.springframework.stereotype.Component;

import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.model.Student;

@Component
public class StudentService {

	private StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void create(Student student) {
		studentDao.create(student);
	}

	public void getById(int id) {
		studentDao.getById(id);
	}

	public void getAll() {
		studentDao.getAll();
	}

	public void update(Student student) {
		studentDao.update(student);
	}

	public void deleteById(Student student) {
		studentDao.deleteById(student.getId());
	}
}
