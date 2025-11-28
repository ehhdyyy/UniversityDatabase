package university.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import university.dao.EnrollmentDAO;

import java.util.List;

@Controller
public class EnrollmentController {
    private final EnrollmentDAO enrollmentDAO;

    public EnrollmentController(EnrollmentDAO enrollmentDAO) {
        this.enrollmentDAO = enrollmentDAO;
    }

    @GetMapping("/enrollments")
    public String showEnrollments(Model model) {
        model.addAttribute("enrollments", enrollmentDAO.findAllEnrollments());
        return "enrollments"; 
    }
}