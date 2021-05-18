package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private final Logger log = LoggerFactory.getLogger(GroupService.class);

	public GroupService(GroupDao groupDao, StudentDao studentDao) {
		this.groupDao = groupDao;
		this.studentDao = studentDao;
	}

	public void create(Group group) {
		if (groupDao.getByName(group) == null) {
			groupDao.create(group);
		} else {
			log.warn("Create error, name {} is not unique", group.getName());
		}
	}

	public Group getById(int id) {
		return groupDao.getById(id);
	}

	public List<Group> getAll() {
		return groupDao.getAll();
	}

	public void update(Group group) {
		if (isGroupExist(group.getId()))  {
			if (isUniqueName(group)) {
			groupDao.update(group);
			} else {
				log.warn("Update error, name {} is not unique", group.getName());
			}
		} else {
			log.warn("Update error, group {} is already exist", group);
		}
	}

	public void deleteById(int id) {
		if (isGroupExist(id) ) {
			if (!isStudentsInGroupExist(id)) { 
			groupDao.deleteById(id);
			} else {
				log.warn("deleteById error, group, id = {} have students",id);
			}
		} else {
			log.warn("Group, id = {}, does not exist",id);			
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
