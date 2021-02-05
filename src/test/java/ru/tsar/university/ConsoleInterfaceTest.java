package ru.tsar.university;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsoleInterfaceTest {

	ConsoleInterface consoleInterface;
	University university;

	@BeforeEach
	void setUp() {
		university = new University();
//		consoleInterface = new ConsoleInterface(university);
	}

	@Test
	void test() {
		// LocalDate
		// consoleInterface.addStudent("firstName", "lastName", "sex", date, "email",
		// "phone", "address");
	}

}
