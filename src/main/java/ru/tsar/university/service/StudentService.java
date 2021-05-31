package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.exceptions.StudentNotExistException;
import ru.tsar.university.model.Student;

@Service
public class StudentService {

	private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);
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
		try {
			isStudentExist(student.getId());
			studentDao.update(student);
		} catch (StudentNotExistException e)  {
			LOG.warn(e.getMessage());
		}
	}

	public void deleteById(int id) {
		try {
		isStudentExist(id);
			studentDao.deleteById(id);
		} catch(StudentNotExistException e)  {
			LOG.warn(e.getMessage());
		}
	}

	public void isStudentExist(int id) throws StudentNotExistException {
		if ( studentDao.getById(id) == null) {
			throw new StudentNotExistException(id);
		}
	}
}
