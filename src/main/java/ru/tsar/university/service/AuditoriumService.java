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
		if (auditoriumDao.getByName(auditorium) == null) {
			auditoriumDao.create(auditorium);
		}
	}

	public Auditorium getById(int id) {
		if (auditoriumDao.getById(id) != null) {
			return auditoriumDao.getById(id);
		} else {
			return null;
		}
	}

	public List<Auditorium> getAll() {
		return auditoriumDao.getAll();
	}

	public void update(Auditorium auditorium) {
		
		if (auditoriumDao.getById(auditorium.getId()) != null  && checkAuditoriumNameFree(auditorium)) {
			auditoriumDao.update(auditorium);
		}
	}

	public void deleteById(int id) {
		if (auditoriumDao.getById(id) != null) {
			auditoriumDao.deleteById(id);
		}
	}

	public boolean checkAuditoriumNameFree(Auditorium auditorium) {
		Auditorium auditoriumByName = auditoriumDao.getByName(auditorium);
		if (auditoriumByName == null) {
			return true;
		} else {
			if (auditoriumByName.getId() == auditorium.getId()) {
				return true;
			}
			return false;
		}
	}
}
