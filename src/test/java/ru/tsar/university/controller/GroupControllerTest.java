package ru.tsar.university.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.tsar.university.controller.GroupControllerTest.TestData.*;

import java.util.ArrayList;
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

import ru.tsar.university.model.Group;
import ru.tsar.university.model.Student;
import ru.tsar.university.service.GroupService;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

	@Mock
	GroupService groupService;

	@InjectMocks
	GroupController groupController;

	MockMvc mockMvc;

	@BeforeEach
	void init() {
		PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
		pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
		pageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(0, 5));
		mockMvc = MockMvcBuilders.standaloneSetup(groupController).setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver).build();
	}

	@Test
	void givenExistingGroupsRequest_whenGetAllGroupsRequestReceived_thenGroupDetailsViewReturned() throws Exception {
		mockMvc.perform(get("/groups")).andExpect(status().isOk()).andExpect(view().name("group/index"));
	}

	@Test
	void givenExistingGroupsRequest_whenGetAllGroupsRequestReceived_thenGroupsPageAttributeRecieved() throws Exception {

		List<Group> groups = Stream.of(group1, group2).collect(Collectors.toList());
		Page<Group> page = new PageImpl<>(groups, PageRequest.of(0, 5), groups.size());

		when(groupService.getAll(pageabele)).thenReturn(page);
		mockMvc.perform(get("/groups")).andExpect(status().isOk()).andExpect(model().attribute("groupsPage", page));
	}

	@Test
	void givenExistingGroupId_whenGetGroupByIdRequestReceived_thenGroupDetailsViewReturned() throws Exception {
		mockMvc.perform(get("/groups/{id}", 1)).andExpect(status().isOk()).andExpect(view().name("group/show"));
	}

	@Test
	void givenExistingGroupId_whenGetGroupByIdRequestReceived_thenAttributeGroupFound() throws Exception {

		when(groupService.getById(1)).thenReturn(group1);

		mockMvc.perform(get("/groups/{id}", 1)).andExpect(status().isOk())
				.andExpect(model().attribute("group", group1));
	}

	interface TestData {
		Pageable pageabele = PageRequest.of(0, 5);
		List<Student> students = new ArrayList<>();
		Group group1 = Group.builder()
				.id(1).name("a1-55")
				.students(students)
				.build();
		Group group2 = Group.builder()
				.id(2).name("a2-53")
				.students(students)
				.build();
	}
}