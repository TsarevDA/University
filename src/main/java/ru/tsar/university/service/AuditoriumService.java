package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.tsar.university.University;
import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.model.Auditorium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuditoriumService {

	private AuditoriumDao auditoriumDao;
	private final Logger log = LoggerFactory.getLogger(AuditoriumService.class);

	public AuditoriumService(AuditoriumDao auditoriumDao) {
		this.auditoriumDao = auditoriumDao;
	}

	public void create(Auditorium auditorium) {
		if (isUniqueName(auditorium)) {
			auditoriumDao.create(auditorium);
		} else {
			log.warn("Create error, name {} is not unique", auditorium.getName());
		}
	}

	public Auditorium getById(int id) {
		return auditoriumDao.getById(id);
	}

	public List<Auditorium> getAll() {
		return auditoriumDao.getAll();
	}

	public void update(Auditorium auditorium) {
		if (isAuditoriumExist(auditorium.getId())) {
			if (isUniqueName(auditorium)) {
			auditoriumDao.update(auditorium);
			} else {
				log.warn("Update error, name {} is not unique", auditorium.getName());
			}
		} else {
			log.warn("Auditorium {} is already exist", auditorium);
		}
	}

	public void deleteById(int id) {
		if (isAuditoriumExist(id)) {
			auditoriumDao.deleteById(id);
		} else {
			log.warn("deteleById error, id = {} is not exist", id);
		}
	}

	public boolean isUniqueName(Auditorium auditorium) {
		Auditorium auditoriumByName = auditoriumDao.getByName(auditorium);
		return (auditoriumByName == null || auditoriumByName.getId() == auditorium.getId());
	}

	public boolean isAuditoriumExist(int id) {
		return auditoriumDao.getById(id) != null;
	}
}
