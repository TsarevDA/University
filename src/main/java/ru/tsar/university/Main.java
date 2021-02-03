package ru.tsar.university;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



@SpringBootApplication
public class Main {	
	public static void main(String[] args) throws IOException {
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
		ConsoleInterface consoleInterface = context.getBean("consoleInterface", ConsoleInterface.class);		
		consoleInterface.startMenu();

	}
}
