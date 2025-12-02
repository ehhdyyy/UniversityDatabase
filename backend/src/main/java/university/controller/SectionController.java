package university.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import university.dao.SectionDAO;
import university.dao.CourseDAO;
import university.model.Section;

import java.util.List;

@Controller
@RequestMapping("/sections")
public class SectionController {
    private final SectionDAO sectionDAO;
    private final CourseDAO courseDAO;

    public SectionController(SectionDAO sectionDAO, CourseDAO courseDAO) {
        this.sectionDAO = sectionDAO;
        this.courseDAO = courseDAO;
    }

    @GetMapping
    public String showSections(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String term,
            Model model) {
        try {
            List<Section> sections;
            
            if (courseId != null) {
                sections = sectionDAO.filterByCourseId(courseId);
            } else if (term != null && !term.isEmpty()) {
                sections = sectionDAO.filterByTerm(term);
            } else {
                sections = sectionDAO.findAllWithDetails();
            }
            
            model.addAttribute("sections", sections);
            return "sections";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading sections: " + e.getMessage());
            model.addAttribute("sections", List.of());
            return "sections";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("section", new Section());
            model.addAttribute("courses", courseDAO.findAll());
            return "section-form";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error loading form: " + e.getMessage());
            return "redirect:/sections";
        }
    }

    @PostMapping("/add")
    public String addSection(@ModelAttribute Section section, RedirectAttributes redirectAttributes) {
        try {
            sectionDAO.insert(section);
            redirectAttributes.addFlashAttribute("message", "Section added successfully!");
            return "redirect:/sections";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error adding section: " + e.getMessage());
            return "redirect:/sections/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Section section = sectionDAO.findByID(id);
            if (section == null) {
                redirectAttributes.addFlashAttribute("error", "Section not found");
                return "redirect:/sections";
            }
            model.addAttribute("section", section);
            model.addAttribute("courses", courseDAO.findAll());
            return "section-form";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error loading section: " + e.getMessage());
            return "redirect:/sections";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateSection(@PathVariable int id, @ModelAttribute Section section, RedirectAttributes redirectAttributes) {
        try {
            section.setSectionID(id);
            sectionDAO.update(section);
            redirectAttributes.addFlashAttribute("message", "Section updated successfully!");
            return "redirect:/sections";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating section: " + e.getMessage());
            return "redirect:/sections/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteSection(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            sectionDAO.delete(id);
            redirectAttributes.addFlashAttribute("message", "Section deleted successfully!");
            return "redirect:/sections";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting section: " + e.getMessage());
            return "redirect:/sections";
        }
    }
}