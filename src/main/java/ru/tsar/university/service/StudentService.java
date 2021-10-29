package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
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

	public void update(Student student) {
		verifyStudentExistence(student.getId());
		studentDao.update(student);

	}

	public void deleteById(int id) {
		verifyStudentExistence(id);
		studentDao.deleteById(id);
	}
	
	public List<Student> getByGroupId(int groupId) {
		return studentDao.getByGroupId(groupId);
	}
	
	public void verifyStudentExistence(int id) {
		if (studentDao.getById(id) == null) {
			throw new EntityNotFoundException("Student with id = " + id + " does not exist");
		}
	}
	
}
