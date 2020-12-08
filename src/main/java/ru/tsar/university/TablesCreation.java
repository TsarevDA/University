package ru.tsar.university;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TablesCreation {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public TablesCreation(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void createTables() throws IOException {
		ScriptReader reader = new ScriptReader();
		List<String> data = new ArrayList<>();
		data = reader.read("schema.sql").collect(Collectors.toList());
		List<String> scripts = collectRequest(data);

		Iterator<String> iterator = scripts.iterator();
		while (iterator.hasNext()) {
			String k = iterator.next();
			jdbcTemplate.update(k);
		}
	}

	public static List<String> collectRequest(List<String> scripts) {
		List<String> result = new ArrayList<>();
		StringBuilder scriptBuilder = new StringBuilder();
		scripts.stream().forEach((s) -> {
			s = s.replaceAll("\\s+$", "");
			if (s.length() != 0) {
				if (s.substring(s.length() - 1).equals(";")) {
					scriptBuilder.append(s);
					result.add(scriptBuilder.toString());
					scriptBuilder.setLength(0);
				} else {
					scriptBuilder.append(s);
				}
			}
		});
		return result;
	}
}
