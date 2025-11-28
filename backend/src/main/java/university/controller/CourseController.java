package university.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import university.dao.CourseDAO;

import java.util.List;

@Controller
public class CourseController {
    private final CourseDAO courseDAO;

    public CourseController(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @GetMapping("/courses")
    public String showEnrollments(Model model) {
        model.addAttribute("courses", courseDAO.findAllCourses());
        return "courses"; 
    }
}   