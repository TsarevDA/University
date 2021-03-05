package ru.tsar.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.tsar.university.model.Auditorium;

@Component
public class AuditoriumRowMapper implements RowMapper<Auditorium> {

	@Override
	public Auditorium mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Auditorium.AuditoriumBuilder().setId(rs.getInt("id")).setName(rs.getString("name")).setCapacity(rs.getInt("capacity")).build();
	}
}
