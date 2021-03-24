package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;

import ru.tsar.university.University;
import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.model.Auditorium;

@Component
public class AuditoriumService {
	
	private AuditoriumDao auditoriumDao;
	
	public AuditoriumService(AuditoriumDao auditoriumDao) {
		this.auditoriumDao = auditoriumDao;
	}

	public void create(Auditorium auditorium) { 
		auditoriumDao.create(auditorium);
	}
	
	public Auditorium getById(Auditorium auditorium) {
		return auditoriumDao.getById(auditorium.getId());
	}
	
	public List<Auditorium> getAll() {
		return auditoriumDao.getAll();
	}
	
	public void update(Auditorium auditorium) {
		auditoriumDao.update(auditorium);
	}
	
	public void deleteById(Auditorium auditorium) {
		auditoriumDao.deleteById(auditorium.getId());
	}	
}
