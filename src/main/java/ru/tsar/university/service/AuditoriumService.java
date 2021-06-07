package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.exceptions.AuditroiumNotExistException;
import ru.tsar.university.exceptions.NotUniqueNameException;
import ru.tsar.university.model.Auditorium;

@Service
public class AuditoriumService {

	private AuditoriumDao auditoriumDao;

	public AuditoriumService(AuditoriumDao auditoriumDao) {
		this.auditoriumDao = auditoriumDao;
	}

	public void create(Auditorium auditorium) {
		verifyNameUniqueness(auditorium);
		auditoriumDao.create(auditorium);
	}

	public Auditorium getById(int id) {
		return auditoriumDao.getById(id);
	}

	public List<Auditorium> getAll() {
		return auditoriumDao.getAll();
	}

	public void update(Auditorium auditorium) {
		verifyAuditoriumExistence(auditorium.getId());
		verifyNameUniqueness(auditorium);
		auditoriumDao.update(auditorium);
	}

	public void deleteById(int id) {
		verifyAuditoriumExistence(id);
		auditoriumDao.deleteById(id);
	}

	public void verifyNameUniqueness(Auditorium auditorium) throws NotUniqueNameException {
		Auditorium auditoriumByName = auditoriumDao.getByName(auditorium);
		if (auditoriumByName != null && auditoriumByName.getId() != auditorium.getId()) {
			throw new NotUniqueNameException("This name is not unique: " + auditorium.getName());
		}
	}

	public void verifyAuditoriumExistence(int id) throws AuditroiumNotExistException {
		if (auditoriumDao.getById(id) == null) {
			throw new AuditroiumNotExistException("Auditorium with id = " + id + " does not exist");
		}
	}

}
