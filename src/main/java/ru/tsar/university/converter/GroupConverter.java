package ru.tsar.university.converter;

import org.springframework.core.convert.converter.Converter;

import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Group;
import ru.tsar.university.service.GroupService;

public class GroupConverter implements Converter<String, Group> {

	private GroupService groupService;

	public GroupConverter(GroupService groupService) {
		this.groupService = groupService;
	}

	@Override
	public Group convert(String id) {
		int parsedId = Integer.parseInt(id);
		return groupService.getById(parsedId);
	}

}
