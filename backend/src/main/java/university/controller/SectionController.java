package university.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import university.dao.SectionDAO;

import java.util.List;

@Controller
public class SectionController {
    private final SectionDAO sectionDAO;

    public SectionController(SectionDAO sectionDAO) {
        this.sectionDAO = sectionDAO;
    }

    @GetMapping("/sections")
    public String showSections(Model model) {
        model.addAttribute("sections", sectionDAO.findAllSections());
        return "sections"; 
    }
}