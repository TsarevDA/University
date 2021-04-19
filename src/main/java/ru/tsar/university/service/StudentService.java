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
		Student student = studentDao.getById(id);
		if (student != null) {
			return student;
		} else {
			return null;
		}
	}

	public List<Student> getAll() {
		return studentDao.getAll();
	}

	public void update(Student student) {
		if (isExistId(student.getId())) {
			studentDao.update(student);
		}
	}

	public void deleteById(int id) {
		if (isExistId(id)) {
			studentDao.deleteById(id);
		}
	}

	public boolean isExistId(int id) {
		return studentDao.getById(id) != null;
	}
}
