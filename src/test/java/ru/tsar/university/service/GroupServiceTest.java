package ru.tsar.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static ru.tsar.university.service.GroupServiceTest.TestData.*;

import org.mockito.junit.jupiter.MockitoExtension;

import ru.tsar.university.dao.GroupDao;
import ru.tsar.university.dao.StudentDao;
import ru.tsar.university.exceptions.EntityNotFoundException;
import ru.tsar.university.exceptions.NotUniqueNameException;
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
		Group group = Group.builder()
				.name("T5-03")
				.build();

		groupService.create(group);

		verify(groupDao).create(group);
	}

	@Test
	void givenNewGroup_whenCreate_thenThrowNotUniqueNameException() {
		Group group = Group.builder()
				.name("T7-09")
				.build();

		when(groupDao.getByName(group)).thenReturn(group_1);

		Exception exception = assertThrows(NotUniqueNameException.class, () -> groupService.create(group));

		String expectedMessage = "This name is not unique: " + group.getName();
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void givenId_whenGetById_thenGroupFound() {
		Group expected = group_1;

		when(groupDao.getById(expected.getId())).thenReturn(expected);

		Group actual = groupService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenGroups_whenGetAll_thenGroupsListFound() {
		Group first = group_1;
		Group second = group_2;
		List<Group> expected = new ArrayList<>();
		expected.add(first);
		expected.add(second);

		when(groupDao.getAll()).thenReturn(expected);

		List<Group> actual = groupService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenGroup_whenUpdate_thenCallDaoMethod() {
		Group newGroup = group_1;
		Group oldGroup = group_3;

		when(groupDao.getById(1)).thenReturn(oldGroup);
		when(groupDao.getByName(newGroup)).thenReturn(oldGroup);

		groupService.update(newGroup);

		verify(groupDao).update(newGroup);
	}

	@Test
	void givenNameDublicateCourse_whenUpdate_thenThrowNotUniqueNameException() {
		Group newGroup = group_1;
		Group oldGroup = group_3;
		Group dublicateGroup = group_2;

		when(groupDao.getById(1)).thenReturn(oldGroup);
		when(groupDao.getByName(newGroup)).thenReturn(dublicateGroup);

		Exception exception = assertThrows(NotUniqueNameException.class, () -> 
			groupService.update(newGroup));

		String expectedMessage = "This name is not unique: " + newGroup.getName();
		String actualMessage = exception.getMessage();

		assertEquals(actualMessage, expectedMessage);

	}

	@Test
	void givenId_whenDeleteById_thenDeleted() {
		Group group = group_3;

		when(groupDao.getById(1)).thenReturn(group);
		when(studentDao.getByGroupId(1)).thenReturn(students_3);

		groupService.deleteById(1);

		verify(groupDao).deleteById(1);
	}

	@Test
	void givenGroupWithStudents_whenDeleteById_thenNoAction() {
		Group group = group_1;

		when(groupDao.getById(1)).thenReturn(group);
		when(studentDao.getByGroupId(1)).thenReturn(students_1);

		groupService.deleteById(1);

		verify(studentDao, never()).deleteById(1);
	}

	@Test
	void givenNotExistedId_whenDeleteById_thenThrowGroupNotExistException() {
		when(groupDao.getById(1)).thenReturn(null);

		Exception exception = assertThrows(EntityNotFoundException.class, () ->
			groupService.deleteById(1));

		String expectedMessage = "Group with id = 1 does not exist";
		String actualMessage = exception.getMessage();
		
		assertEquals(actualMessage, expectedMessage);
	}

	interface TestData {
		Student student_1 = Student.builder()
				.id(1)
				.firstName("Ivan")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.email("mail@mail.ru")
				.phone("88008080")
				.address("Ivanov street, 25-5")
				.build();
		Student student_2 = Student.builder()
				.id(1)
				.firstName("Petr")
				.lastName("Petrov")
				.gender(Gender.valueOf("MALE"))
				.email("mail11@mail.ru")
				.phone("8800")
				.address("Petrov street, 25-5")
				.build();
		List<Student> students_1 = Arrays.asList(student_1);
		List<Student> students_2 = Arrays.asList(student_2);
		List<Student> students_3 = new ArrayList<>();
		Group group_1 = Group.builder()
				.id(1)
				.name("T7-09")
				.students(students_1)
				.build();
		Group group_2 = Group.builder()
				.id(2)
				.name("T7-10")
				.students(students_2)
				.build();
		Group group_3 = Group.builder()
				.id(1)
				.name("T7-10")
				.students(students_3)
				.build();
	}
}
