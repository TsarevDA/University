package ru.tsar.university.service;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import ru.tsar.university.dao.AuditoriumDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
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

	public Page<Auditorium> getAll(Pageable pageable) {
		return auditoriumDao.getAll(pageable);
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

	public void verifyNameUniqueness(Auditorium auditorium) {
		Auditorium auditoriumByName = auditoriumDao.getByName(auditorium);
		if (auditoriumByName != null && auditoriumByName.getId() != auditorium.getId()) {
			throw new NotUniqueNameException("This name is not unique: " + auditorium.getName());
		}
	}

	public void verifyAuditoriumExistence(int id) {
		if (auditoriumDao.getById(id) == null) {
			throw new EntityNotFoundException("Auditorium with id = " + id + " does not exist");
		}
	}

}
