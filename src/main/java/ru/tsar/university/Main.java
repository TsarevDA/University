package ru.tsar.university;

import java.io.IOException;
import org.springframework.jdbc.core.JdbcTemplate;

public class Main {

	public static void main(String[] args) throws IOException {

		ConnectionProvider connectionProvider = new ConnectionProvider();
		JdbcTemplate jdbcTemplate = connectionProvider.getJdbcTemplate();
		TablesCreation tablesCreation = new TablesCreation(jdbcTemplate);
		tablesCreation.createTables();
		University university = new University();
		ConsoleInterface consoleInterface = new ConsoleInterface(university, jdbcTemplate);
		consoleInterface.startMenu();

	}
}
