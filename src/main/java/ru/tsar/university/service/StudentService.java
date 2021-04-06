package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.model.Student;

@Service
public class StudentService {

	private StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void create(Student student) {
		studentDao.create(student);
	}

	public Student getById(int id) {
		if (studentDao.checkIdExist(id)) {
		return studentDao.getById(id);
		} else {
			return null;
		}
	}

	public List<Student> getAll() {
		return studentDao.getAll();
	}

	public void update(Student student) {
		if (studentDao.checkIdExist(student.getId())) {
		studentDao.update(student);
		}
	}

	public void deleteById(int id) {
		if (studentDao.checkIdExist(id)) {
		studentDao.deleteById(id);
		}
	}
}
