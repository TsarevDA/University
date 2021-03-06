package ru.tsar.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.tsar.university.controller.TeacherControllerTest.TestData.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

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

import ru.tsar.university.service.CourseService;
import ru.tsar.university.service.TeacherService;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Teacher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

	@Mock
	private TeacherService teacherService;

	@Mock
	private CourseService courseService;

	@InjectMocks
	private TeacherController teacherController;

	MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
		pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
		pageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(0, 5));
		mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
				.setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver).build();
	}

	@Test
	public void givenExistingTeachersRequest_whenGetAllTeachersRequestReceived_thenTeachersDetailsViewReturned()
			throws Exception {
		mockMvc.perform(get("/teachers")).andExpect(status().isOk()).andExpect(view().name("teacher/index"));
	}

	@Test
	public void givenExistingTeachersRequest_whenGetAllTeachersRequestReceived_thenTeachersPageAttributeRecieved()
			throws Exception {
		List<Course> courses = Stream.of(course1, course2).collect(Collectors.toList());
		teacher1.setCourses(courses);
		teacher2.setCourses(courses);
		List<Teacher> teachers = Stream.of(teacher1, teacher2).collect(Collectors.toList());
		Page<Teacher> page = new PageImpl<>(teachers, PageRequest.of(0, 5), teachers.size());

		when(teacherService.getAll(pageabele)).thenReturn(page);
		mockMvc.perform(get("/teachers")).andExpect(status().isOk()).andExpect(model().attribute("teachersPage", page));
	}

	@Test
	public void givenExistingTeacherId_whenGetCourseByIdRequestReceived_thenTeacherDetailsViewReturned()
			throws Exception {
		mockMvc.perform(get("/teachers/{id}", 1)).andExpect(status().isOk()).andExpect(view().name("teacher/show"));
	}

	@Test
	public void givenExistingTeacherId_whenGetTeacherByIdRequestReceived_thenAttributeTeacherFound() throws Exception {

		List<Course> courses = Stream.of(course1, course2).collect(Collectors.toList());
		Teacher teacher = teacher1;
		teacher.setCourses(courses);

		when(teacherService.getById(1)).thenReturn(teacher1);

		mockMvc.perform(get("/teachers/{id}", 1)).andExpect(model().attribute("teacher", teacher));
	}

	@Test
	public void givenNewAuditorium_whenCreatePostRequest_thenCreated() throws Exception {
		mockMvc.perform(post("/teachers/create").flashAttr("teacher", teacher3)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teachers"));
		verify(teacherService).create(teacher3);
	}

	@Test
	public void givenUpdatedTeacher_whenSavePostRequest_thenUpdated() throws Exception {
		mockMvc.perform(post("/teachers/save").flashAttr("teacher", teacher1)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teachers"));
		verify(teacherService).update(teacher1);
	}

	@Test
	public void givenExistingId_whenDeletePostRequest_thenDeleted() throws Exception {
		mockMvc.perform(get("/teachers/delete").param("id", "1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teachers"));
		verify(teacherService).deleteById(1);
	}

	@Test
	public void givenCreateTeacherRequest_whenCreate_thenCreateFormViewReturned() throws Exception {
		when(courseService.getAll()).thenReturn(courses);
		mockMvc.perform(get("/teachers/new")).andExpect(status().isOk()).andExpect(view().name("teacher/new"));
	}

	@Test
	public void givenUpdateTeacherRequest_whenUpdate_thenUpdateViewReturned() throws Exception {
		when(teacherService.getById(1)).thenReturn(teacher1);
		when(courseService.getAll()).thenReturn(courses);
		mockMvc.perform(get("/teachers/update").param("id", "1")).andExpect(status().isOk())
				.andExpect(view().name("teacher/update"));
	}

	interface TestData {
		
		Pageable pageabele = PageRequest.of(0, 5);
		List<Course> courses = new ArrayList<>();
		Course course1 = Course.builder()
				.id(1)
				.name("math")
				.description("About digits")
				.build();
		Course course2 = Course.builder()
				.id(2).name("biology")
				.description("name about nature")
				.build();
		Teacher teacher1 = Teacher.builder()
				.id(1)
				.firstName("Bob")
				.lastName("Kelso")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1990, 10, 10))
				.address("Street")
				.email("bob@dot.mail")
				.phone("111")
				.build();
		Teacher teacher2 = Teacher.builder()
				.id(2)
				.firstName("Mark")
				.lastName("Socket")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1991, 11, 11))
				.address("HiStreet")
				.email("mark@mark.ru")
				.phone("1111")
				.build();
		Teacher teacher3 = Teacher.builder()
				.firstName("Mark")
				.lastName("Socket")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1991, 11, 11))
				.address("HiStreet")
				.email("mark@mark.ru")
				.phone("1111")
				.build();
	}
}