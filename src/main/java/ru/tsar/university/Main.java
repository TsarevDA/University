package ru.tsar.university;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.init.DatabasePopulator;

import ru.tsar.university.config.SpringConfig;

public class Main {

	public static void main(String[] args) throws IOException {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
		context.getBean("databasePopulator", DatabasePopulator.class);

		ConsoleInterface consoleInterface = context.getBean("consoleInterface", ConsoleInterface.class);
		consoleInterface.startMenu();
	}
}
