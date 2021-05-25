package ru.tsar.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.exceptions.AuditroiumExistException;
import ru.tsar.university.exceptions.UniqueNameException;
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

	public void create(Auditorium auditorium) throws UniqueNameException {
		if (isUniqueName(auditorium)) {
			auditoriumDao.create(auditorium);
		} else {
			throw new UniqueNameException(auditorium.getName());
		}
	}

	public Auditorium getById(int id) {
		return auditoriumDao.getById(id);
	}

	public List<Auditorium> getAll() {
		return auditoriumDao.getAll();
	}

	public void update(Auditorium auditorium) throws  AuditroiumExistException {
		if (isAuditoriumExist(auditorium.getId())) {
			if (isUniqueName(auditorium)) {
			auditoriumDao.update(auditorium);
			} else {
				LOG.warn("Update error, name {} is not unique", auditorium.getName());
			}
		} else {
			throw new AuditroiumExistException(auditorium);
		}
	}

	public void deleteById(int id) throws AuditroiumExistException {
		if (isAuditoriumExist(id)) {
			auditoriumDao.deleteById(id);
		} else {
			throw new AuditroiumExistException(id);
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
