package ru.tsar.university;

public class Main {

	public static void main(String[] args) {
		
		University university = new University();
		ConsoleInterface consoleInterface = new ConsoleInterface(university);
		consoleInterface.startMenu();

	}

}
