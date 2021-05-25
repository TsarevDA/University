package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.exceptions.StudentExistException;
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
		return studentDao.getById(id);
	}

	public List<Student> getAll() {
		return studentDao.getAll();
	}

	public void update(Student student) throws StudentExistException {
		if (isStudentExist(student.getId())) {
			studentDao.update(student);
		} else {
			throw new StudentExistException(student);
		}
	}

	public void deleteById(int id) throws StudentExistException {
		if (isStudentExist(id)) {
			studentDao.deleteById(id);
		} else {
			throw new StudentExistException(id);
		}
	}

	public boolean isStudentExist(int id) {
		return studentDao.getById(id) != null;
	}
}
