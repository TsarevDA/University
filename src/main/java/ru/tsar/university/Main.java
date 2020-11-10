package ru.tsar.university;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		University university = new University();
		ConsoleInterface consoleInterface = new ConsoleInterface(university);
		consoleInterface.startMenu();
	}
}
