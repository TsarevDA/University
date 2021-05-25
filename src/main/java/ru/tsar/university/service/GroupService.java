package ru.tsar.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.exceptions.GroupExistException;
import ru.tsar.university.exceptions.UniqueNameException;
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

	public void create(Group group) throws UniqueNameException {
		if (groupDao.getByName(group) == null) {
			groupDao.create(group);
		} else {
			throw new UniqueNameException(group.getName());
		}
	}

	public Group getById(int id) {
		return groupDao.getById(id);
	}

	public List<Group> getAll() {
		return groupDao.getAll();
	}

	public void update(Group group) throws GroupExistException, UniqueNameException {
		if (isGroupExist(group.getId()))  {
			if (isUniqueName(group)) {
			groupDao.update(group);
			} else {
				throw new UniqueNameException(group.getName());
				//LOG.warn("Update error, name {} is not unique", group.getName());
			}
		} else {
			throw new GroupExistException(group);
			//LOG.warn("Update error, group {} is already exist", group);
		}
	}

	public void deleteById(int id) throws GroupExistException {
		if (isGroupExist(id) ) {
			if (!isStudentsInGroupExist(id)) { 
			groupDao.deleteById(id);
			} else {
				LOG.warn("deleteById error, group, id = {} have students",id);
			}
		} else {
			throw new GroupExistException(id);		
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
