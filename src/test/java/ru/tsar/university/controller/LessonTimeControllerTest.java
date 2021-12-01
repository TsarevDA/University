package ru.tsar.university.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.tsar.university.controller.LessonTimeControllerTest.TestData.*;

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

import ru.tsar.university.model.LessonTime;
import ru.tsar.university.service.LessonTimeService;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class LessonTimeControllerTest {

	@Mock
	LessonTimeService lessonTimeService;

	@InjectMocks
	LessonTimeController lessonTimeController;

	MockMvc mockMvc;

	@BeforeEach
	void init() {
		PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
		pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
		pageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(0, 5));
		mockMvc = MockMvcBuilders.standaloneSetup(lessonTimeController)
				.setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver).build();
	}

	@Test
	void givenExistingLessonTimesRequest_whenGetAllLessonTimesRequestReceived_thenLessonTimesDetailsViewReturned()
			throws Exception {
		mockMvc.perform(get("/lessontimes")).andExpect(status().isOk()).andExpect(view().name("lessonTime/index"));
	}

	@Test
	void givenExistingLessonTimesRequest_whenGetAllLessonTimesRequestReceived_thenLessonTimesPageAttributeRecieved()
			throws Exception {

		List<LessonTime> lessonTimes = Stream.of(lessonTime1, lessonTime2).collect(Collectors.toList());
		Page<LessonTime> page = new PageImpl<>(lessonTimes, PageRequest.of(0, 5), lessonTimes.size());

		when(lessonTimeService.getAll(pageabele)).thenReturn(page);
		mockMvc.perform(get("/lessontimes")).andExpect(status().isOk())
				.andExpect(model().attribute("lessonTimesPage", page));
	}

	@Test
	void givenExistingLessonTimeId_whenGetLessonTimeByIdRequestReceived_thenLessonTimeDetailsViewReturned()
			throws Exception {
		mockMvc.perform(get("/lessontimes/{id}", 1)).andExpect(status().isOk())
				.andExpect(view().name("lessonTime/show"));
	}

	@Test
	void givenExistingLessonTimeId_whenGetLessonTimeByIdRequestReceived_thenAttributeLessonTimeFound()
			throws Exception {

		when(lessonTimeService.getById(1)).thenReturn(lessonTime1);

		mockMvc.perform(get("/lessontimes/{id}", 1)).andExpect(status().isOk())
				.andExpect(model().attribute("lessonTime", lessonTime1));
	}

	interface TestData {
		
		Pageable pageabele = PageRequest.of(0, 5);
		LessonTime lessonTime1 = LessonTime.builder()
				.id(1)
				.orderNumber(1)
				.startTime(LocalTime.of(8, 0))
				.endTime(LocalTime.of(9, 0))
				.build();
		LessonTime lessonTime2 = LessonTime.builder()
				.id(2)
				.orderNumber(2)
				.startTime(LocalTime.of(9, 0))
				.endTime(LocalTime.of(10, 0))
				.build();
	}
}
