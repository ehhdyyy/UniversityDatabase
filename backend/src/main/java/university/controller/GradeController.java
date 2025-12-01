package university.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import university.dao.GradeDAO;
import university.dao.EnrollmentDAO;
import university.model.Grade;

import java.util.List;

@Controller
@RequestMapping("/grades")
public class GradeController {
    private final GradeDAO gradeDAO;
    private final EnrollmentDAO enrollmentDAO;

    public GradeController(GradeDAO gradeDAO, EnrollmentDAO enrollmentDAO) {
        this.gradeDAO = gradeDAO;
        this.enrollmentDAO = enrollmentDAO;
    }

    @GetMapping
    public String showGrades(
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String grade,
            Model model) {
        try {
            List<Grade> grades;
            
            if (studentName != null && !studentName.isEmpty()) {
                grades = gradeDAO.filterByStudentName(studentName);
            } else if (grade != null && !grade.isEmpty()) {
                grades = gradeDAO.filterByGrade(grade);
            } else {
                grades = gradeDAO.findAllWithDetails();
            }
            
            model.addAttribute("grades", grades);
            return "grades";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading grades: " + e.getMessage());
            model.addAttribute("grades", List.of());
            return "grades";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        try {
            model.addAttribute("grade", new Grade());
            model.addAttribute("enrollments", enrollmentDAO.findAll());
            return "grade-form";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading form: " + e.getMessage());
            return "redirect:/grades";
        }
    }

    @PostMapping("/add")
    public String addGrade(@ModelAttribute Grade grade, RedirectAttributes redirectAttributes) {
        try {
            gradeDAO.insert(grade);
            redirectAttributes.addFlashAttribute("message", "Grade added successfully!");
            return "redirect:/grades";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding grade: " + e.getMessage());
            return "redirect:/grades/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        try {
            Grade grade = gradeDAO.findByID(id);
            if (grade == null) {
                model.addAttribute("error", "Grade not found");
                return "redirect:/grades";
            }
            model.addAttribute("grade", grade);
            model.addAttribute("enrollments", enrollmentDAO.findAll());
            return "grade-form";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading grade: " + e.getMessage());
            return "redirect:/grades";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateGrade(@PathVariable int id, @ModelAttribute Grade grade, RedirectAttributes redirectAttributes) {
        try {
            grade.setGradeID(id);
            gradeDAO.update(grade);
            redirectAttributes.addFlashAttribute("message", "Grade updated successfully!");
            return "redirect:/grades";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating grade: " + e.getMessage());
            return "redirect:/grades/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteGrade(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            gradeDAO.delete(id);
            redirectAttributes.addFlashAttribute("message", "Grade deleted successfully!");
            return "redirect:/grades";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting grade: " + e.getMessage());
            return "redirect:/grades";
        }
    }
}