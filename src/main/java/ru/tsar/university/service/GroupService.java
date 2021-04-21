package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Group;

@Service
public class GroupService {

	private GroupDao groupDao;
	private StudentDao studentDao;

	public GroupService(GroupDao groupDao, StudentDao studentDao) {
		this.groupDao = groupDao;
		this.studentDao = studentDao;
	}

	public void create(Group group) {
		if (groupDao.getByName(group) == null) {
			groupDao.create(group);
		}
	}

	public Group getById(int id) {
		return groupDao.getById(id);
	}

	public List<Group> getAll() {
		return groupDao.getAll();
	}

	public void update(Group group) {
		if (isGroupExist(group.getId()) && isUniqueName(group)) {
			groupDao.update(group);
		}
	}

	public void deleteById(int id) {
		if (isGroupExist(id) && !isStudentsInGroupExist(id)) {
			groupDao.deleteById(id);
		}
	}

	public boolean isUniqueName(Group group) {
		Group groupByName = groupDao.getByName(group);
		return (groupByName == null || groupByName.getId() == group.getId());
	}

	public boolean isStudentsInGroupExist(int id) {
		return (studentDao.getByGroupId(id) == null);
	}

	public boolean isGroupExist(int id) {
		return groupDao.getById(id) != null;
	}
}
