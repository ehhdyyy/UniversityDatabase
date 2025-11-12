package university.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import university.dao.StudentDAO;

import java.util.List;

@Controller
public class StudentController {
    private final StudentDAO studentDAO;

    public StudentController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @GetMapping("/students")
    public String showStudents(Model model) {
        model.addAttribute("students", studentDAO.findAllNames());
        return "students"; 
    }
}