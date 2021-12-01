package ru.tsar.university.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UniversityControllerTest {

	MockMvc mockMvc;

	@Test
	void givenUniversityRequest_whenUniversityRequestReceived_thenUniversityDetailsViewReturned() throws Exception {
		UniversityController controller = new UniversityController();
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("university/index"));
	}
}