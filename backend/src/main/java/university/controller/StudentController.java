package university.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import university.dao.StudentDAO;
import university.model.Student;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentDAO studentDAO;

    public StudentController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @GetMapping
    public String showStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String major,
            Model model) {
        try {
            List<Student> students;
            
            if (search != null && !search.isEmpty()) {
                students = studentDAO.searchByName(search);
            } else if (major != null && !major.isEmpty()) {
                students = studentDAO.filterByMajor(major);
            } else {
                students = studentDAO.findAll();
            }
            
            model.addAttribute("students", students);
            return "students";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading students: " + e.getMessage());
            model.addAttribute("students", List.of());
            return "students";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        try {
            studentDAO.insert(student);
            redirectAttributes.addFlashAttribute("message", "Student added successfully!");
            return "redirect:/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding student: " + e.getMessage());
            return "redirect:/students/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        try {
            Student student = studentDAO.findByID(id);
            if (student == null) {
                model.addAttribute("error", "Student not found");
                return "redirect:/students";
            }
            model.addAttribute("student", student);
            return "student-form";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading student: " + e.getMessage());
            return "redirect:/students";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable int id, @ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        try {
            student.setStudentID(id);
            studentDAO.update(student);
            redirectAttributes.addFlashAttribute("message", "Student updated successfully!");
            return "redirect:/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating student: " + e.getMessage());
            return "redirect:/students/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            studentDAO.delete(id);
            redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");
            return "redirect:/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting student: " + e.getMessage());
            return "redirect:/students";
        }
    }
}