package ru.tsar.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.tsar.university.controller.AuditoriumControllerTest.TestData.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import ru.tsar.university.model.Auditorium;
import ru.tsar.university.service.AuditoriumService;

@ExtendWith(MockitoExtension.class)
class AuditoriumControllerTest {

	@InjectMocks
	private AuditoriumController auditoriumController;
	@Mock
	private AuditoriumService auditoriumService;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
		pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
		pageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(0, 5));
		this.mockMvc = MockMvcBuilders.standaloneSetup(auditoriumController)
				.setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver).build();
	}

	@Test
	public void givenExistingAuditoriumsRequest_whenGetAllAuditoriumsRequestReceived_thenAuditoriumDetailsViewReturned()
			throws Exception {
		mockMvc.perform(get("/auditoriums")).andExpect(status().isOk()).andExpect(view().name("auditorium/index"));
	}

	@Test
	public void givenExistingAuditoriumsRequest_whenGetAllAuditoriumsRequestReceived_thenAuditoriumsPageAttributeRecieved()
			throws Exception {
		List<Auditorium> auditoriums = new ArrayList<>();
		auditoriums.add(Auditorium.builder().name("name").capacity(100).build());
		auditoriums.add(Auditorium.builder().name("name2").capacity(10).build());

		Page<Auditorium> page = new PageImpl<>(auditoriums, pageabele, auditoriums.size());

		when(auditoriumService.getAll(pageabele)).thenReturn(page);

		mockMvc.perform(get("/auditoriums")).andExpect(status().isOk())
				.andExpect(model().attribute("auditoriumsPage", page));
	}

	@Test
	public void givenExistingAuditoriumId_whenGetAuditoriumByIdRequestReceived_thenAuditoriumDetailsViewReturned()
			throws Exception {
		Auditorium auditorium = Auditorium.builder().id(1).name("name").capacity(100).build();
		when(auditoriumService.getById(1)).thenReturn(auditorium);
		mockMvc.perform(get("/auditoriums/{id}", 1)).andExpect(status().isOk())
				.andExpect(view().name("auditorium/show"));
	}

	@Test
	public void givenExistingAuditoriumId_whenGetAuditoriumByIdRequestReceived_thenAttributeAuditoriumFound()
			throws Exception {
		Auditorium auditorium = Auditorium.builder().id(1).name("name").capacity(100).build();
		when(auditoriumService.getById(anyInt())).thenReturn(auditorium);
		mockMvc.perform(get("/auditoriums/{id}", 1)).andExpect(status().isOk())
				.andExpect(model().attribute("auditorium", auditorium));
	}

	interface TestData {
		Pageable pageabele = PageRequest.of(0, 5);
	}
}