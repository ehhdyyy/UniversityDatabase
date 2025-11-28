package university.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import university.dao.GradeDAO;

import java.util.List;

@Controller
public class GradeController {
    private final GradeDAO gradeDAO;

    public GradeController(GradeDAO gradeDAO) {
        this.gradeDAO = gradeDAO;
    }

    @GetMapping("/grades")
    public String showGrades(Model model) {
        model.addAttribute("grades", gradeDAO.findAllGrades());
        return "grades"; 
    }
}