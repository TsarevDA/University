package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.model.Group;

@Service
public class GroupService {

	private GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void create(Group group) {
		if (!groupDao.checkNameExist(group)) {
			groupDao.create(group);
		}
	}

	public Group getById(int id) {
		if (groupDao.checkIdExist(id)) {
		return groupDao.getById(id);
		} else {
			return null;
		}
	}

	public List<Group> getAll() {
		return groupDao.getAll();
	}

	public void update(Group group) {
		if (groupDao.checkIdExist(group.getId())) {
			groupDao.update(group);
		}
	}

	public void deleteById(int id) {
		if (groupDao.checkIdExist(id)) {
		groupDao.deleteById(id);
		}
	}
}
