package ru.tsar.university.controller;

import static org.mockito.Mockito.verify;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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
		PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
		pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
		pageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(0, 5));
		mockMvc = MockMvcBuilders.standaloneSetup(studentController)
				.setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver).build();
	}

	@Test
	void givenExistingStudentsRequest_whenGetAllStudentsRequestReceived_thenStudentsDetailsViewReturned()
			throws Exception {
		mockMvc.perform(get("/students")).andExpect(status().isOk()).andExpect(view().name("student/index"));
	}

	@Test
	void givenExistingStudentsRequest_whenGetAllStudentsRequestReceived_thenStudentsPageAttributeRecieved()
			throws Exception {

		List<Student> students = Stream.of(student1, student2).collect(Collectors.toList());
		Page page = new PageImpl<>(students, PageRequest.of(0, 5), students.size());

		when(studentService.getAll(pageabele)).thenReturn(page);
		mockMvc.perform(get("/students")).andExpect(status().isOk()).andExpect(model().attribute("studentsPage", page));
	}

	@Test
	void givenExistingStudentId_whenGetStudentByIdRequestReceived_thenStudentDetailsViewReturned() throws Exception {
		mockMvc.perform(get("/students/{id}", 1)).andExpect(status().isOk()).andExpect(view().name("student/show"));
	}

	@Test
	void givenExistingStudentId_whenGetStudentByIdRequestReceived_thenAttributeStudentFound() throws Exception {

		when(studentService.getById(1)).thenReturn(student1);

		mockMvc.perform(get("/students/{id}", 1)).andExpect(status().isOk())
				.andExpect(model().attribute("student", student1));
	}

	@Test
	public void givenNewStudent_whenCreatePostRequest_thenCallServiceMethod() throws Exception {
		mockMvc.perform(post("/students/create").flashAttr("student", student3)).andExpect(status().is3xxRedirection());
		verify(studentService).create(student3);
	}

	@Test
	public void givenUpdatedStudent_whenSavePostRequest_thenCallServiceMethod() throws Exception {
		mockMvc.perform(post("/students/save").flashAttr("student", student1)).andExpect(status().is3xxRedirection());
		verify(studentService).update(student1);
	}

	@Test
	public void givenExistingId_whenDeletePostRequest_thenCallServiceMethod() throws Exception {
		mockMvc.perform(get("/students/delete?id=1")).andExpect(status().is3xxRedirection());
		verify(studentService).deleteById(1);
	}

	@Test
	public void givenCreateStudentRequest_whenCreate_thenCreateFormViewReturned() throws Exception {
		mockMvc.perform(get("/students/new")).andExpect(status().isOk()).andExpect(view().name("student/new"));
	}

	@Test
	public void givenUpdateStudentRequest_whenUpdate_thenUpdateViewReturned() throws Exception {
		mockMvc.perform(get("/students/update?id=1")).andExpect(status().isOk())
				.andExpect(view().name("student/update"));
	}

	interface TestData {
		
		Pageable pageabele = PageRequest.of(0, 5);
		Student student1 = Student.builder()
				.id(1)
				.firstName("Bob")
				.lastName("Kelso")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1990, 10, 10))
				.address("Street").email("bob@dot.mail")
				.phone("111")
				.build();
		Student student2 = Student.builder()
				.id(2).firstName("Mark")
				.lastName("Socket")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1991, 11, 11))
				.address("HiStreet")
				.email("mark@mark.ru")
				.phone("1111")
				.build();
		Student student3 = Student.builder()
				.firstName("Bob")
				.lastName("Kelso")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1990, 10, 10))
				.address("Street").email("bob@dot.mail")
				.phone("111")
				.build();
	}
}