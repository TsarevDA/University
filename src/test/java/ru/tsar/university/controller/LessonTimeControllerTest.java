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
		mockMvc = MockMvcBuilders.standaloneSetup(lessonTimeController).build();
	}
	
	@Test
	void givenLessonTimeURI_whenMockMvc_thenReturnIndexHtml() throws Exception {
		mockMvc.perform(get("/lessontimes")).andExpect(status().isOk()).andExpect(view().name("lessonTime/index"));
	}
	
	@Test
	void givenLesonTimeURI_whenMockMvc_thenVerifyResponse() throws Exception {
		
		List<LessonTime> lessonTimes = Stream.of(lessonTime1,lessonTime2).collect(Collectors.toList());
		
		when(lessonTimeService.getAll()).thenReturn(lessonTimes);
		
		Page<LessonTime> page = new PageImpl<>(lessonTimes, PageRequest.of(0, 5),lessonTimes.size());
		
		mockMvc.perform(get("/lessontimes")).andExpect(status().isOk()).andExpect(model().attribute("lessonTimesPage", page));
	}
	
	@Test
	void givenLessonTimeURIwithPathVariable_whenMockMvc_thenReturnShowHtml() throws Exception {
		mockMvc.perform(get("/lessontimes/{id}",1)).andExpect(status().isOk()).andExpect(view().name("lessonTime/show"));
	}
	
	@Test
	void givenLessonTimeURIwithPathVariable_whenMockMvc_thenVerifyResponse() throws Exception {
		
		when(lessonTimeService.getById(1)).thenReturn(lessonTime1);
		
		mockMvc.perform(get("/lessontimes/{id}",1)).andExpect(status().isOk()).andExpect(model().attribute("lessonTime", lessonTime1));
	}
	
	interface TestData {
		LessonTime lessonTime1 = LessonTime.builder().id(1).orderNumber(1).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(9, 0)).build();
		LessonTime lessonTime2 = LessonTime.builder().id(2).orderNumber(2).startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(10, 0)).build();
	}
}
