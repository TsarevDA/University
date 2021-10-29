package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.exceptions.NotUniqueNameException;
import ru.tsar.university.model.Group;

@Service
public class GroupService {

	private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);
	private GroupDao groupDao;
	private StudentDao studentDao;

	public GroupService(GroupDao groupDao, StudentDao studentDao) {
		this.groupDao = groupDao;
		this.studentDao = studentDao;
	}

	public void create(Group group) {
		verifyNameUniqueness(group);
		groupDao.create(group);
	}

	public Group getById(int id) {
		return groupDao.getById(id);
	}

	public List<Group> getAll() {
		return groupDao.getAll();
	}

	public void update(Group group) {
		verifyGroupExist(group.getId());
		verifyNameUniqueness(group);
		groupDao.update(group);
	}

	public void deleteById(int id) {
		verifyGroupExist(id);
		if (!isStudentsInGroupExistence(id)) {
			groupDao.deleteById(id);
		} else {
			LOG.warn("deleteById error, group, id = {} have students", id);
		}
	}

	public List<Group> getByLessonId(int lessonId) {
		return groupDao.getByLessonId(lessonId);
	}
	
	
	public void verifyNameUniqueness(Group group) throws NotUniqueNameException {
		Group groupByName = groupDao.getByName(group);
		if (groupByName != null && groupByName.getId() != group.getId()) {
			throw new NotUniqueNameException("This name is not unique: " + group.getName());
		}
	}

	public boolean isStudentsInGroupExistence(int id) {
		return (studentDao.getByGroupId(id) == null);
	}

	public void verifyGroupExist(int id) {
		if (groupDao.getById(id) == null) {
			throw new EntityNotFoundException("Group with id = " + id + " does not exist");
		}
	}
}
