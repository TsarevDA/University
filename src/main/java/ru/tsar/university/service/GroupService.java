package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;

import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.model.Group;

@Component
public class GroupService {
	
	private GroupDao groupDao;
	
	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
	
	public void create(Group group) {
		groupDao.create(group);
	}
	
	public Group getById(Group group) {
		return groupDao.getById(group.getId());
	}
	
	public List<Group> getAll(){
		return groupDao.getAll();
	}
	
	public void update(Group group) {
		groupDao.update(group);		
	}
	
	public void deleteById(Group group) {
		groupDao.deleteById(group.getId());
	}
}
