package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.exceptions.AuditroiumNotExistException;
import ru.tsar.university.exceptions.NotUniqueNameException;
import ru.tsar.university.model.Auditorium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuditoriumService {

	private static final Logger LOG = LoggerFactory.getLogger(AuditoriumService.class);
	private AuditoriumDao auditoriumDao;

	public AuditoriumService(AuditoriumDao auditoriumDao) {
		this.auditoriumDao = auditoriumDao;
	}

	public void create(Auditorium auditorium) {
		try {
			isUniqueName(auditorium);
			auditoriumDao.create(auditorium);
		} catch (NotUniqueNameException e) {
			LOG.warn(e.getMessage());
		}
	}

	public Auditorium getById(int id) {
		return auditoriumDao.getById(id);
	}

	public List<Auditorium> getAll() {
		return auditoriumDao.getAll();
	}

	public void update(Auditorium auditorium) {
		try {
			isAuditoriumExist(auditorium.getId());
			isUniqueName(auditorium);
			auditoriumDao.update(auditorium);
		} catch (NotUniqueNameException | AuditroiumNotExistException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void deleteById(int id) {
		try {
			isAuditoriumExist(id);
			auditoriumDao.deleteById(id);
		} catch (AuditroiumNotExistException e) {
			LOG.warn(e.getMessage());
		}
	}

	public void isUniqueName(Auditorium auditorium) throws NotUniqueNameException {
		Auditorium auditoriumByName = auditoriumDao.getByName(auditorium);
		if (auditoriumByName != null) {
			if (auditoriumByName.getId() != auditorium.getId()) {
				throw new NotUniqueNameException(auditorium.getName());
			}
		}
	}

	public void isAuditoriumExist(int id) throws AuditroiumNotExistException {
		if (auditoriumDao.getById(id) == null) {
			throw new AuditroiumNotExistException(id);
		}
	}
}
