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
import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.model.Auditorium;

@ExtendWith(MockitoExtension.class)
class AuditoriumServiceTest {

	@InjectMocks
	private AuditoriumService auditoriumService;
	@Mock
	private AuditoriumDao auditoriumDao;

	@Test
	void givenExistAuditorium_whenCreate_thenNoAction() {
		Auditorium auditorium = Auditorium.builder().name("A1000").capacity(100).build();
		Auditorium existAuditorium = Auditorium.builder().id(1).name("A1000").capacity(100).build();
		
		when(auditoriumDao.getByName(auditorium)).thenReturn(existAuditorium);

		auditoriumService.create(auditorium);

		verify(auditoriumDao, never()).create(auditorium);
	}

	@Test
	void givenNewAuditorium_whenCreate_thenCallDaoMethod() {
		Auditorium expected = Auditorium.builder().name("A1000").capacity(100).build();

		when(auditoriumDao.getByName(expected)).thenReturn(null);

		auditoriumService.create(expected);

		verify(auditoriumDao).create(expected);
	}

	@Test
	void givenId_whenGetById_thenAuditoriumFound() {
		Auditorium expected = new Auditorium.AuditoriumBuilder().id(1).name("First").capacity(100).build();

		when(auditoriumDao.getById(anyInt())).thenReturn(expected);

		Auditorium actual = auditoriumService.getById(1);
		assertEquals(expected, actual);
	}

	@Test
	void givenAuditoriums_whenGetAll_thenAuditoriumsListFound() {
		Auditorium auditorium1 = Auditorium.builder().id(1).name("First").capacity(100).build();
		Auditorium auditorium2 = Auditorium.builder().id(2).name("Second").capacity(500).build();
		List<Auditorium> expected = new ArrayList<>();
		expected.add(auditorium1);
		expected.add(auditorium2);

		when(auditoriumDao.getAll()).thenReturn(expected);

		List<Auditorium> actual = auditoriumService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenAuditorium_whenUpdate_thenCallDaoMethod() {
		Auditorium expected = Auditorium.builder().id(1).name("newAuditorium").capacity(1000).build();
		Auditorium oldValue = Auditorium.builder().id(1).name("newAuditorium").capacity(100).build();

		when(auditoriumDao.getById(1)).thenReturn(oldValue);
		when(auditoriumDao.getByName(expected)).thenReturn(oldValue);

		auditoriumService.update(expected);
		verify(auditoriumDao).update(expected);
		;

	}

	@Test
	void givenNameDublicateAuditorium_whenUpdate_thenNoAction() {
		Auditorium newAuditorium = Auditorium.builder().id(1).name("newAuditorium").capacity(1000).build();
		Auditorium oldAuditorium = Auditorium.builder().id(1).name("Auditorium").capacity(100).build();
		Auditorium dublicateAuditorium = Auditorium.builder().id(2).name("newAuditorium").capacity(100).build();

		when(auditoriumDao.getById(1)).thenReturn(oldAuditorium);
		when(auditoriumDao.getByName(newAuditorium)).thenReturn(dublicateAuditorium);

		auditoriumService.update(newAuditorium);
		verify(auditoriumDao, never()).update(newAuditorium);
	}

	@Test
	void givenId_whenDeleteById_thenCallDaoMethod() {
		Auditorium auditorium = Auditorium.builder().id(1).name("A1000").capacity(100).build();

		when(auditoriumDao.getById(1)).thenReturn(auditorium);

		auditoriumService.deleteById(1);

		verify(auditoriumDao).getById(1);
	}
}