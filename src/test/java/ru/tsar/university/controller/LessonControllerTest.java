package ru.tsar.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.tsar.university.controller.LessonControllerTest.TestData.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.util.LinkedMultiValueMap;

import ru.tsar.university.model.Auditorium;
import ru.tsar.university.model.Course;
import ru.tsar.university.model.Gender;
import ru.tsar.university.model.Group;
import ru.tsar.university.model.Lesson;
import ru.tsar.university.model.LessonTime;
import ru.tsar.university.model.Student;
import ru.tsar.university.model.Teacher;
import ru.tsar.university.service.AuditoriumService;
import ru.tsar.university.service.CourseService;
import ru.tsar.university.service.GroupService;
import ru.tsar.university.service.LessonService;
import ru.tsar.university.service.LessonTimeService;
import ru.tsar.university.service.TeacherService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

	@Mock
	LessonService lessonService;
	@Mock
	private CourseService courseService;
	@Mock
	private AuditoriumService auditoriumService;
	@Mock
	private TeacherService teacherService;
	@Mock
	private GroupService groupService;
	@Mock
	private LessonTimeService lessonTimeService;

	@InjectMocks
	LessonController lessonController;

	MockMvc mockMvc;

	@BeforeEach
	void init() {
		PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
		pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
		pageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(0, 5));
		mockMvc = MockMvcBuilders.standaloneSetup(lessonController)
				.setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver).build();
	}

	@Test
	void givenExistingLessonsRequest_whenGetAllLessonsRequestReceived_thenLessonDetailsViewReturned() throws Exception {
		mockMvc.perform(get("/lessons")).andExpect(status().isOk()).andExpect(view().name("lesson/index"));
	}

	@Test
	void givenExistingLessonsRequest_whenGetAllLessonsRequestReceived_thenLessonsPageAttributeRecieved()
			throws Exception {

		List<Lesson> lessons = Stream.of(lesson).collect(Collectors.toList());
		Page<Lesson> page = new PageImpl<>(lessons, PageRequest.of(0, 5), lessons.size());

		when(lessonService.getAll(pageabele)).thenReturn(page);
		mockMvc.perform(get("/lessons")).andExpect(status().isOk()).andExpect(model().attribute("lessonsPage", page));
	}

	@Test
	void givenExistingLessonId_whenGetLessonByIdRequestReceived_thenLessonDetailsViewReturned() throws Exception {
		mockMvc.perform(get("/lessons/{id}", 1)).andExpect(status().isOk()).andExpect(view().name("lesson/show"));
	}

	@Test
	void givenExistingLessonId_whenGetLessonByIdRequestReceived_thenAttributeLessonFound() throws Exception {
		when(lessonService.getById(1)).thenReturn(lesson);
		mockMvc.perform(get("/lessons/{id}/", 1)).andExpect(status().isOk())
				.andExpect(model().attribute("lesson", lesson));
	}

	@Test
	public void givenNewLesson_whenCreatePostRequest_thenCreated() throws Exception {
		when(courseService.getById(anyInt())).thenReturn(course);
		when(teacherService.getById(anyInt())).thenReturn(teacher);
		when(auditoriumService.getById(anyInt())).thenReturn(auditorium);
		when(groupService.getById(anyInt())).thenReturn(group);
		when(lessonTimeService.getById(anyInt())).thenReturn(lessonTime);

		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		requestParams.add("courseId", "1");
		requestParams.add("teacherId", "1");
		requestParams.add("day", "08.12.2020");
		requestParams.add("auditoriumId", "1");
		requestParams.add("groupId", "1");
		requestParams.add("lessonTimeId", "1");
		mockMvc.perform(post("/lessons/create").params(requestParams)).andExpect(status().is3xxRedirection());

		verify(lessonService).create(lesson2);
	}

	@Test
	public void givenUpdatedLesson_whenSavePostRequest_thenUpdated() throws Exception {
		mockMvc.perform(post("/lessons/save").flashAttr("lesson", lesson)).andExpect(status().is3xxRedirection());
		verify(lessonService).update(lesson);
	}

	@Test
	public void givenExistingId_whenDeletePostRequest_thenDeleted() throws Exception {
		mockMvc.perform(get("/lessons/delete?id=1")).andExpect(status().is3xxRedirection());
		verify(lessonService).deleteById(1);
	}

	@Test
	public void givenCreateLessonRequest_whenCreate_thenCreateFormViewReturned() throws Exception {
		mockMvc.perform(get("/lessons/new")).andExpect(status().isOk()).andExpect(view().name("lesson/new"));
	}

	@Test
	public void givenUpdateLessonRequest_whenUpdate_thenUpdateViewReturned() throws Exception {
		mockMvc.perform(get("/lessons/update?id=1")).andExpect(status().isOk()).andExpect(view().name("lesson/update"));
	}
	
	interface TestData {
		
		Pageable pageabele = PageRequest.of(0, 5);
		List<Student> students = new ArrayList<>();
		Group group = Group.builder().id(1).name("T7-09").students(students).build();
		List<Group> groups = Arrays.asList(group);

		Auditorium auditorium = Auditorium.builder()
				.id(1)
				.name("First")
				.capacity(100)
				.build();
		Course course = Course.builder()
				.id(1).name("Astronomy")
				.description("Science about stars and deep space")
				.build();
		List<Course> teacherCourses = Arrays.asList(course);

		Teacher teacher = Teacher.builder()
				.id(1).firstName("Petr")
				.lastName("Ivanov")
				.gender(Gender.valueOf("MALE"))
				.birthDate(LocalDate.of(1992, Month.MAY, 03))
				.email("mail11111@mail.ru")
				.phone("880899908080")
				.address("Petrov street, 25-5")
				.courses(teacherCourses)
				.build();
		LocalTime startTime = LocalTime.of(9, 0);
		LocalTime endTime = LocalTime.of(10, 0);
		LessonTime lessonTime = LessonTime.builder().id(1).orderNumber(2).startTime(startTime).endTime(endTime).build();

		LocalDate day = LocalDate.of(2020, Month.DECEMBER, 8);
		Lesson lesson = Lesson.builder()
				.id(1)
				.course(course)
				.teacher(teacher)
				.group(groups)
				.day(day)
				.time(lessonTime)
				.auditorium(auditorium)
				.build();
		
		Lesson lesson2 = Lesson.builder()
				.course(course)
				.teacher(teacher)
				.group(groups)
				.day(day)
				.time(lessonTime)
				.auditorium(auditorium)
				.build();
	}
}