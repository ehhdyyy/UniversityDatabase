package university.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import university.dao.EnrollmentDAO;
import university.dao.StudentDAO;
import university.dao.SectionDAO;
import university.model.Enrollment;

import java.util.List;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollmentDAO enrollmentDAO;
    private final StudentDAO studentDAO;
    private final SectionDAO sectionDAO;

    public EnrollmentController(EnrollmentDAO enrollmentDAO, StudentDAO studentDAO, SectionDAO sectionDAO) {
        this.enrollmentDAO = enrollmentDAO;
        this.studentDAO = studentDAO;
        this.sectionDAO = sectionDAO;
    }

    @GetMapping
    public String showEnrollments(
            @RequestParam(required = false) Integer studentId,
            @RequestParam(required = false) Integer sectionId,
            Model model) {
        try {
            List<Enrollment> enrollments;
            
            if (studentId != null) {
                enrollments = enrollmentDAO.filterByStudentId(studentId);
            } else if (sectionId != null) {
                enrollments = enrollmentDAO.filterBySectionId(sectionId);
            } else {
                enrollments = enrollmentDAO.findAllWithDetails();
            }
            
            model.addAttribute("enrollments", enrollments);
            return "enrollments";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading enrollments: " + e.getMessage());
            model.addAttribute("enrollments", List.of());
            return "enrollments";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        try {
            model.addAttribute("enrollment", new Enrollment());
            model.addAttribute("students", studentDAO.findAll());
            model.addAttribute("sections", sectionDAO.findAll());
            return "enrollment-form";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading form: " + e.getMessage());
            return "redirect:/enrollments";
        }
    }

    @PostMapping("/add")
    public String addEnrollment(@ModelAttribute Enrollment enrollment, RedirectAttributes redirectAttributes) {
        try {
            enrollmentDAO.insert(enrollment);
            redirectAttributes.addFlashAttribute("message", "Enrollment added successfully!");
            return "redirect:/enrollments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding enrollment: " + e.getMessage());
            return "redirect:/enrollments/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        try {
            Enrollment enrollment = enrollmentDAO.findByID(id);
            if (enrollment == null) {
                model.addAttribute("error", "Enrollment not found");
                return "redirect:/enrollments";
            }
            model.addAttribute("enrollment", enrollment);
            model.addAttribute("students", studentDAO.findAll());
            model.addAttribute("sections", sectionDAO.findAll());
            return "enrollment-form";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading enrollment: " + e.getMessage());
            return "redirect:/enrollments";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateEnrollment(@PathVariable int id, @ModelAttribute Enrollment enrollment, RedirectAttributes redirectAttributes) {
        try {
            enrollment.setEnrollmentID(id);
            enrollmentDAO.update(enrollment);
            redirectAttributes.addFlashAttribute("message", "Enrollment updated successfully!");
            return "redirect:/enrollments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating enrollment: " + e.getMessage());
            return "redirect:/enrollments/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            enrollmentDAO.delete(id);
            redirectAttributes.addFlashAttribute("message", "Enrollment deleted successfully!");
            return "redirect:/enrollments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting enrollment: " + e.getMessage());
            return "redirect:/enrollments";
        }
    }
}