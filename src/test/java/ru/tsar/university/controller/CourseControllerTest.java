package ru.tsar.university.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.tsar.university.controller.CourseControllerTest.TestData.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ru.tsar.university.model.Course;
import ru.tsar.university.service.CourseService;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

	@Mock
	CourseService courseService;

	@InjectMocks
	CourseController courseController;

	MockMvc mockMvc;

	@BeforeEach
	void init() {
		PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
		pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
		pageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(0, 5));
		mockMvc = MockMvcBuilders.standaloneSetup(courseController)
				.setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver).build();
	}

	@Test
	void givenExistingCoursesRequest_whenGetAllCoursesRequestReceived_thenCourseDetailsViewReturned() throws Exception {
		mockMvc.perform(get("/courses")).andExpect(status().isOk()).andExpect(view().name("course/index"));
	}

	@Test
	void givenExistingCoursesRequest_whenGetAllCoursesRequestReceived_thenCoursesPageAttributeRecieved()
			throws Exception {

		List<Course> courses = Stream.of(course1, course2).collect(Collectors.toList());
		Page<Course> page = new PageImpl<>(courses, pageabele, courses.size());

		when(courseService.getAll(pageabele)).thenReturn(page);

		mockMvc.perform(get("/courses")).andExpect(status().isOk()).andExpect(model().attribute("coursesPage", page));
	}

	@Test
	void givenExistingCourseId_whenGetCourseByIdRequestReceived_thenCourseDetailsViewReturned() throws Exception {
		mockMvc.perform(get("/courses/{id}", 1)).andExpect(status().isOk()).andExpect(view().name("course/show"));
	}

	@Test
	void givenExistingCourseId_whenGetCourseByIdRequestReceived_thenAttributeCourseFound() throws Exception {
		when(courseService.getById(1)).thenReturn(course1);
		mockMvc.perform(get("/courses/{id}", 1)).andExpect(status().isOk())
				.andExpect(model().attribute("course", course1));
	}

	interface TestData {
		Pageable pageabele = PageRequest.of(0, 5);
		Course course1 = Course.builder()
				.id(1).name("math")
				.description("about digits")
				.build();
		Course course2 = Course.builder()
				.id(2).name("astronomy")
				.description("about stars")
				.build();
	}
}
