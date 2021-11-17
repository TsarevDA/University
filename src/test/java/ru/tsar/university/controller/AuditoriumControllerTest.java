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
import org.springframework.data.domain.Sort;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
	this.mockMvc = MockMvcBuilders.standaloneSetup(auditoriumController).build();
	}
	
	
	@Test
	public void givenAuditoriumURI_whenMockMVC_thenReturnsIndexHtml() throws Exception {
		mockMvc.perform(get("/auditoriums")).andExpect(status().isOk())
	      .andExpect(view().name("auditorium/index"));
	}
	
	@Test
	public void givenAuditoriumURI_whenMockMVC_thenVerifyResponse() throws Exception {
		List<Auditorium> auditoriums = new ArrayList<>();
		auditoriums.add(Auditorium.builder().name("name").capacity(100).build());
		auditoriums.add(Auditorium.builder().name("name2").capacity(10).build());
	  
		when(auditoriumService.getAll()).thenReturn(auditoriums);

		Page<Auditorium> page = new PageImpl<>(auditoriums,
				PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "capacity")), auditoriums.size());
		
		 mockMvc.perform(get("/auditoriums")).andExpect(status().isOk())
		.andExpect(model().attribute("auditoriumsPage", page));
	}
	
	@Test
	public void givenAuditoriumURIWithPathVariable_whenMockMVC_thenReturnsShowHtml() throws Exception {
		mockMvc.perform(get("/auditoriums/{id}",1)).andExpect(status().isOk())
	      .andExpect(view().name("auditorium/show"));
	}
	
	@Test
	public void givenAuditoriumURIWithPathVariable_whenMockMVC_thenVerifyResponse() throws Exception {
		Auditorium auditorium  = Auditorium.builder().id(1).name("name").capacity(100).build();
		when(auditoriumService.getById(1)).thenReturn(auditorium);
		mockMvc.perform(get("/auditoriums/{id}",1)).andExpect(status().isOk())
		.andExpect(model().attribute("auditorium", auditorium));
	}
	
}
