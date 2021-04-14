package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

	@InjectMocks
	private GroupService groupService;
	@Mock
	private GroupDao groupDao;
	@Mock
	private StudentDao studentDao;

	@Test
	void givenNewGroup_whenCreate_thenCallDaoMethod() {
		Group group = Group.builder().name("T5-03").build();

		groupService.create(group);

		Mockito.verify(groupDao).create(group);
	}

	@Test
	void givenId_whenGetById_thenGroupFound() {
		Group expected = Group.builder().id(2).name("A7-98").build();
		ArrayList<Student> students = new ArrayList<>();
		expected.setStudents(students);

		Mockito.when(groupDao.getById(expected.getId())).thenReturn(expected);

		Group actual = groupService.getById(2);

		assertEquals(expected, actual);
	}

	@Test
	void givenGroups_whenGetAll_thenGroupsListFound() {
		Group first = Group.builder().id(1).name("T7-09").build();
		Group second = Group.builder().id(2).name("A7-98").build();
		List<Group> expected = new ArrayList<>();
		expected.add(first);
		expected.add(second);

		Mockito.when(groupDao.getAll()).thenReturn(expected);

		List<Group> actual = groupService.getAll();

		assertEquals(expected, actual);
	}
	
	@Test
	void givenGroup_whenUpdate_thenCallDaoMethod() {
		Group newGroup = Group.builder().id(1).name("T7-09").build();
		Group oldGroup = Group.builder().id(1).name("T7-10").build();

		Mockito.when(groupDao.getById(1)).thenReturn(oldGroup);
		Mockito.when(groupDao.getByName(newGroup)).thenReturn(oldGroup);

		groupService.update(newGroup);
		
		Mockito.verify(groupDao).update(newGroup);
	}

	@Test
	void givenNameDublicateCourse_whenUpdate_thenNoAction() {
		Group newGroup =  Group.builder().id(1).name("T7-09").build();
		Group oldGroup =  Group.builder().id(1).name("T7-10").build();
		Group dublicateGroup =  Group.builder().id(2).name("T7-10").build();

		Mockito.when(groupDao.getById(1)).thenReturn(oldGroup);
		Mockito.when(groupDao.getByName(newGroup)).thenReturn(dublicateGroup);

		groupService.update(newGroup);
		Mockito.verify(groupDao, Mockito.never()).update(newGroup);
	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {
		Group group =  Group.builder().id(1).name("T7-09").build();
		
		Mockito.when(groupDao.getById(1)).thenReturn(group);
		groupService.deleteById(1);

		Mockito.verify(groupDao).deleteById(1);
	}

	@Test
	void givenGroupWithStudents_whenDeleteById_thenNoAction() {
		Group group =  Group.builder().id(1).name("T7-09").build();
		Student student = Student.builder().id(1).firstName("Ivan").lastName("Ivanov").gender(Gender.valueOf("MALE")).
			email("mail@mail.ru").phone("88008080").address("Ivanov street, 25-5").build();
		List<Student> students = new ArrayList<> ();
		students.add(student);
		
		Mockito.when(groupDao.getById(1)).thenReturn(group);
		Mockito.when(studentDao.getByGroupId(1)).thenReturn(students);
		
		groupService.deleteById(1);

		Mockito.verify(studentDao,Mockito.never()).deleteById(1);
	}
}
