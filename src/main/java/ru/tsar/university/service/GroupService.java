package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.exceptions.GroupNotExistException;
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
		try {
			isUniqueName(group);
			groupDao.create(group);
		} catch (NotUniqueNameException e) {
			LOG.warn(e.getMessage());
		}
	}

	public Group getById(int id) {
		return groupDao.getById(id);
	}

	public List<Group> getAll() {
		return groupDao.getAll();
	}

	public void update(Group group) {
		try {
			isGroupExist(group.getId());
			isUniqueName(group);
			groupDao.update(group);
		} catch (NotUniqueNameException | GroupNotExistException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void deleteById(int id) {
		try {
			isGroupExist(id);
			if (!isStudentsInGroupExist(id)) {
				groupDao.deleteById(id);
			} else {
				LOG.warn("deleteById error, group, id = {} have students", id);
			}
		} catch (GroupNotExistException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void isUniqueName(Group group) throws NotUniqueNameException {
		Group groupByName = groupDao.getByName(group);
		if (groupByName != null) {
			if (groupByName.getId() != group.getId()) {
				throw new NotUniqueNameException(group.getName());
			}
		}
	}

	public boolean isStudentsInGroupExist(int id) {
		return (studentDao.getByGroupId(id) == null);
	}

	public void isGroupExist(int id) throws GroupNotExistException {
		if (groupDao.getById(id) == null) {
			throw new GroupNotExistException(id);
		}
	}
}
