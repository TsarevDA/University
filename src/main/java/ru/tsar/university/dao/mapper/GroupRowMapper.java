package ru.tsar.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Group;

@Component
public class GroupRowMapper implements RowMapper<Group> {

	@Override
	public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
		Group group = new Group(rs.getInt("id"), rs.getString("name"));
		return group;
	}

}
