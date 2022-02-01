package ru.tsar.university.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.tsar.university.model.Course;
import ru.tsar.university.model.Teacher;
import ru.tsar.university.service.CourseService;
import ru.tsar.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	private TeacherService teacherService;
	private CourseService courseService;
	private List<Course> courseList = new ArrayList<>();

	public TeacherController(TeacherService teacherService, CourseService courseService) {
		this.teacherService = teacherService;
		this.courseService = courseService;
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable int id, Model model) {

		model.addAttribute("teacher", teacherService.getById(id));
		return ("teacher/show");
	}

	@GetMapping
	public String getAll(Model model, Pageable pageable) {

		Page<Teacher> teacherPage = teacherService.getAll(pageable);
		model.addAttribute("teachersPage", teacherPage);
		return ("teacher/index");
	}

	@GetMapping("/courses")
	public String getGroupStudents(@RequestParam(value = "teacherId", required = false) Integer teacherId, Model model,
			Pageable pageable) {

		model.addAttribute("coursesPage", courseService.getByTeacherId(teacherId, pageable));
		return ("course/index");
	}

	@GetMapping("/new")
	public String setTeacher(Model model) {
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("courseList", courseList);
		return ("teacher/new");
	}

	@PostMapping("/addCourse")
	public String addCourseToTeacher(@RequestParam("courseId") int courseId, @RequestParam("teacherId") int teacherId) {
		Course course = courseService.getById(courseId);
		if (!courseList.contains(course)) {
			courseList.add(course);
		}
		if (teacherId > 0)
			return ("redirect:/teachers/update" + "?" + "id=" + teacherId);
		return ("redirect:/teachers/new");
	}

	@PostMapping("/removeCourse")
	public String removeCourseFromTeacher(@RequestParam("courseId") int courseId,
			@RequestParam("teacherId") int teacherId) {
		courseList.remove(courseService.getById(courseId));
		if (teacherId > 0)
			return ("redirect:/teachers/update" + "?" + "id=" + teacherId);
		return ("redirect:/teachers/new");
	}

	@PostMapping("/create")
	public String createTeacher(@ModelAttribute Teacher teacher) {
		teacher.setCourses(courseList);
		teacherService.create(teacher);
		courseList.clear();
		return "redirect:/teachers";
	}

	@GetMapping("/update")
	public String updateTeacher(@RequestParam int id, Model model) {
		Teacher teacher = teacherService.getById(id);
		model.addAttribute("teacher", teacher);
		if (courseList.size() == 0) {
			courseList = teacher.getCourses();
		}
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("courseList", courseList);
		return ("teacher/update");
	}

	@GetMapping("/delete")
	public String deleteTeacher(@RequestParam int id) {
		teacherService.deleteById(id);
		return ("redirect:/teachers");
	}

	@PostMapping("/save")
	public String saveTeacherUpdate(@ModelAttribute Teacher teacher) {
		teacher.setCourses(courseList);
		teacherService.update(teacher);
		courseList.clear();
		return "redirect:/teachers";
	}
}