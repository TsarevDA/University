package ru.tsar.university.service;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	public Page<Student> getAll(Pageable pageable) {
		return studentDao.getAll(pageable);
	}

	public void update(Student student) {
		verifyStudentExistence(student.getId());
		studentDao.update(student);

	}

	public void deleteById(int id) {
		verifyStudentExistence(id);
		studentDao.deleteById(id);
	}

	public Page<Student> getByGroupId(int groupId, Pageable pageable ) {
		return studentDao.getByGroupId(groupId, pageable);
	}

	public void verifyStudentExistence(int id) {
		if (studentDao.getById(id) == null) {
			throw new EntityNotFoundException("Student with id = " + id + " does not exist");
		}
	}

}
