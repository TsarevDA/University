package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.University;
import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.model.Auditorium;

@Service
public class AuditoriumService {

	private AuditoriumDao auditoriumDao;

	public AuditoriumService(AuditoriumDao auditoriumDao) {
		this.auditoriumDao = auditoriumDao;
	}

	public void create(Auditorium auditorium) {
		auditorium.setId(0);
		if (isUniqueName(auditorium)) {
			auditoriumDao.create(auditorium);
		}
	}

	public Auditorium getById(int id) {
		Auditorium auditorium = auditoriumDao.getById(id);
		if (auditorium != null) {
			return auditorium;
		} else {
			return null;
		}
	}

	public List<Auditorium> getAll() {
		return auditoriumDao.getAll();
	}

	public void update(Auditorium auditorium) {
		if (isIdExist(auditorium.getId()) && isUniqueName(auditorium)) {
			auditoriumDao.update(auditorium);
		}
	}

	public void deleteById(int id) {
		if (isIdExist(id)) {
			auditoriumDao.deleteById(id);
		}
	}

	public boolean isUniqueName(Auditorium auditorium) {
		Auditorium auditoriumByName = auditoriumDao.getByName(auditorium);
		return (auditoriumByName == null || auditoriumByName.getId() == auditorium.getId());
	}

	public boolean isIdExist(int id) {
		return auditoriumDao.getById(id) != null;
	}
}
