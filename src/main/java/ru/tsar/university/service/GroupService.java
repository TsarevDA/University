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
		if (groupDao.getById(id) != null) {
		return groupDao.getById(id);
		} else {
			return null;
		}
	}

	public List<Group> getAll() {
		return groupDao.getAll();
	}

	public void update(Group group) {
		if (groupDao.getById(group.getId()) != null && checkGroupNameFree(group)) {
			groupDao.update(group);
		}
	}

	public void deleteById(int id) {
		if (groupDao.getById(id) != null && !checkExistStudentsInGroup(id)) { 
		groupDao.deleteById(id);
		}
	}
	
	public boolean checkGroupNameFree(Group group) {
		Group groupByName = groupDao.getByName(group);
		if (groupByName == null) {
			return true;
		} else {
			if (groupByName.getId() == group.getId()) {
				return true;
			}
			return false;
		}
	}
	
	public boolean checkExistStudentsInGroup(int id) {
		if (studentDao.getByGroupId(id) == null) {
			return true;
		}
		return false;
		
	}
}
