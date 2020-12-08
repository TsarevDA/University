package ru.tsar.university;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class Main {

	public static void main(String[] args) throws IOException {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
		// context.getBean(name : "tablesCreation",TablesCreation.class);
		TablesCreation tablesCreation = context.getBean("tablesCreation", TablesCreation.class);
		tablesCreation.createTables();
		ConsoleInterface consoleInterface = context.getBean("consoleInterface", ConsoleInterface.class);
		consoleInterface.startMenu();

		// ConnectionProvider connectionProvider = new ConnectionProvider();
		// JdbcTemplate jdbcTemplate = connectionProvider.getJdbcTemplate();
		// TablesCreation tablesCreation = new TablesCreation(jdbcTemplate);
		// tablesCreation.createTables();
		// University university = new University();
		// ConsoleInterface consoleInterface = new ConsoleInterface(university,
		// jdbcTemplate);
		// consoleInterface.startMenu();

	}
}
