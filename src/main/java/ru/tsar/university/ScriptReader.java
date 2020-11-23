package ru.tsar.university;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class ScriptReader {
	
	public Stream<String> read(String scriptName) throws IOException {
		if (getClass().getClassLoader().getResource(scriptName) == null) {
			throw new IllegalArgumentException("File " + scriptName + " does not exist.");
		}

		Path path = Paths.get(getClass().getClassLoader().getResource(scriptName).getPath().substring(1));
		return Files.lines(path).filter(Objects::nonNull);

	}
}
