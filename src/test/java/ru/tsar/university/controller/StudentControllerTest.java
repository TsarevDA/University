package ru.tsar.university.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.tsar.university.controller.StudentControllerTest.TestData.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Student;
import ru.tsar.university.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {
	
	@Mock
	StudentService studentService;
	
	@InjectMocks
	StudentController studentController;
	
	MockMvc mockMvc;
	
	@BeforeEach
	void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
	}
	
	@Test
	void givenStudentsURI_whenMockMvc_thenReturnIndexHtml () throws Exception {
		
		mockMvc.perform(get("/students")).andExpect(status().isOk()).andExpect(view().name("student/index"));
	}
	
	@Test
	void givenStudentURI_whenMockMvc_thenVerifyResponse() throws Exception {
		
		List<Student> students = Stream.of(student1,student2).collect(Collectors.toList());
		Page page = new PageImpl<>(students, PageRequest.of(0, 5),students.size());
		
		when(studentService.getAll()).thenReturn(students);
		
		mockMvc.perform(get("/students")).andExpect(status().isOk()).andExpect(model().attribute("studentsPage", page));
	}
	
	@Test
	void givenStudentURIWithPathVariable_whenMockMvc_then_ReturnShowHml() throws Exception {
		mockMvc.perform(get("/students/{id}",1)).andExpect(status().isOk()).andExpect(view().name("student/show"));
	}
	
	@Test
	void givenStudentURIWithPathVariable_whenMockMvc_thenVerifyResponse() throws Exception {
		
		when(studentService.getById(1)).thenReturn(student1);
		
		mockMvc.perform(get("/students/{id}",1)).andExpect(status().isOk()).andExpect(model().attribute("student", student1));
	}
	
	
	
	interface TestData {
		Student student1 = Student.builder().id(1).firstName("Bob").lastName("Kelso").gender(Gender.MALE).birthDate(LocalDate.of(1990, 10, 10)).address("Street").email("bob@dot.mail").phone("111").build();
		Student student2 = Student.builder().id(2).firstName("Mark").lastName("Socket").gender(Gender.MALE).birthDate(LocalDate.of(1991, 11, 11)).address("HiStreet").email("mark@mark.ru").phone("1111").build();
	}
	

}
