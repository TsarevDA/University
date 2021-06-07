package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static ru.tsar.university.service.AuditoriumServiceTest.TestData.*;

import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.exceptions.AuditroiumNotExistException;
import ru.tsar.university.exceptions.NotUniqueNameException;
import ru.tsar.university.model.Auditorium;

@ExtendWith(MockitoExtension.class)
class AuditoriumServiceTest {

	@InjectMocks
	private AuditoriumService auditoriumService;
	@Mock
	private AuditoriumDao auditoriumDao;

	@Test
	void givenExistAuditorium_whenCreate_thenThrowNotUniqueNameException() {
		Auditorium auditorium = Auditorium
				.builder()
				.name("Auditorium")
				.capacity(1000)
				.build();
		
		when(auditoriumDao.getByName(auditorium)).thenReturn(auditorium_1);
		
		Exception exception = assertThrows(NotUniqueNameException.class, () -> {
			auditoriumService.create(auditorium);
		    });
		
		String expectedMessage = "This name is not unique: " + auditorium.getName();
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void givenNewAuditorium_whenCreate_thenCallDaoMethod() {
		Auditorium expected = Auditorium.builder()
				.name("A1000")
				.capacity(100)
				.build();

		when(auditoriumDao.getByName(expected)).thenReturn(null);

		auditoriumService.create(expected);

		verify(auditoriumDao).create(expected);
	}

	@Test
	void givenId_whenGetById_thenAuditoriumFound() {
		when(auditoriumDao.getById(anyInt())).thenReturn(auditorium_1);

		Auditorium actual = auditoriumService.getById(1);
		assertEquals(auditorium_1, actual);
	}

	@Test
	void givenAuditoriums_whenGetAll_thenAuditoriumsListFound() {
		List<Auditorium> expected = new ArrayList<>();
		expected.add(auditorium_1);
		expected.add(dublicateAuditorium_2);

		when(auditoriumDao.getAll()).thenReturn(expected);

		List<Auditorium> actual = auditoriumService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenAuditorium_whenUpdate_thenCallDaoMethod() {
		when(auditoriumDao.getById(1)).thenReturn(auditorium_1);
		when(auditoriumDao.getByName(auditorium_2)).thenReturn(auditorium_1);

		auditoriumService.update(auditorium_2);

		verify(auditoriumDao).update(auditorium_2);
	}

	@Test
	void givenNameDublicateAuditorium_whenUpdate_thenThrowNotUniqueNameException() {
		when(auditoriumDao.getById(1)).thenReturn(auditorium_1);
		when(auditoriumDao.getByName(auditorium_2)).thenReturn(dublicateAuditorium_2);

		Exception exception = assertThrows(NotUniqueNameException.class, () -> {
			auditoriumService.update(auditorium_2);
		    });
		
		String expectedMessage = "This name is not unique: " + auditorium_2.getName();
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		when(auditoriumDao.getById(1)).thenReturn(auditorium_1);

		auditoriumService.deleteById(1);

		verify(auditoriumDao).getById(1);
	}
	
	@Test
	void givenNotExistedId_whenDeleteById_thenThrowAuditroiumNotExistException() {
		when(auditoriumDao.getById(1)).thenReturn(null);

		Exception exception = assertThrows(AuditroiumNotExistException.class, () -> {
			auditoriumService.deleteById(1);
		    });
		
		String expectedMessage = "Auditorium with id = 1 does not exist";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	interface TestData {
		Auditorium auditorium_1 = Auditorium
				.builder()
				.id(1)
				.name("Auditorium")
				.capacity(1000)
				.build();
		Auditorium auditorium_2 = Auditorium.builder()
				.id(1)
				.name("newAuditorium")
				.capacity(100)
				.build();
		Auditorium dublicateAuditorium_2 = Auditorium.builder()
				.id(2)
				.name("newAuditorium")
				.capacity(100)
				.build();
	}
}
