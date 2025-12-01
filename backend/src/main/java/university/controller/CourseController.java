package university.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import university.dao.CourseDAO;
import university.model.Course;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseDAO courseDAO;

    public CourseController(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @GetMapping
    public String showCourses(@RequestParam(required = false) String search, Model model) {
        try {
            List<Course> courses;
            if (search != null && !search.isEmpty()) {
                courses = courseDAO.searchCourses(search);
            } else {
                courses = courseDAO.findAll();
            }
            model.addAttribute("courses", courses);
            return "courses";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading courses: " + e.getMessage());
            model.addAttribute("courses", List.of());
            return "courses";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        try {
            courseDAO.insert(course);
            redirectAttributes.addFlashAttribute("message", "Course added successfully!");
            return "redirect:/courses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding course: " + e.getMessage());
            return "redirect:/courses/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        try {
            Course course = courseDAO.findByID(id);
            if (course == null) {
                model.addAttribute("error", "Course not found");
                return "redirect:/courses";
            }
            model.addAttribute("course", course);
            return "course-form";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading course: " + e.getMessage());
            return "redirect:/courses";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable int id, @ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        try {
            course.setCourseID(id);
            courseDAO.update(course);
            redirectAttributes.addFlashAttribute("message", "Course updated successfully!");
            return "redirect:/courses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating course: " + e.getMessage());
            return "redirect:/courses/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            courseDAO.delete(id);
            redirectAttributes.addFlashAttribute("message", "Course deleted successfully!");
            return "redirect:/courses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting course: " + e.getMessage());
            return "redirect:/courses";
        }
    }
}