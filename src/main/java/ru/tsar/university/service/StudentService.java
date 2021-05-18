package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.model.Student;

@Service
public class StudentService {

	private StudentDao studentDao;
	private final Logger log = LoggerFactory.getLogger(StudentService.class);

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void create(Student student) {
		studentDao.create(student);
	}

	public Student getById(int id) {
		return studentDao.getById(id);
	}

	public List<Student> getAll() {
		return studentDao.getAll();
	}

	public void update(Student student) {
		if (isStudentExist(student.getId())) {
			studentDao.update(student);
		} else {
			log.warn("Update error, student, id = {} is not exist", student.getId());
		}
	}

	public void deleteById(int id) {
		if (isStudentExist(id)) {
			studentDao.deleteById(id);
		} else {
			log.warn("deleteById error, student, id = {} is not exist", id);
		}
	}

	public boolean isStudentExist(int id) {
		return studentDao.getById(id) != null;
	}
}
