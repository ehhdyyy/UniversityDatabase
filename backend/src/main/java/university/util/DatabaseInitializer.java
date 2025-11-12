package university.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import university.dao.*;
import university.model.*;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final CourseDAO courseDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final GradeDAO gradeDAO;
    private final SectionDAO sectionDAO;
    private final StudentDAO studentDAO;

    public DatabaseInitializer(
            CourseDAO courseDAO,
            EnrollmentDAO enrollmentDAO,
            GradeDAO gradeDAO,
            SectionDAO sectionDAO,
            StudentDAO studentDAO
    ) {
        this.courseDAO = courseDAO;
        this.enrollmentDAO = enrollmentDAO;
        this.gradeDAO = gradeDAO;
        this.sectionDAO = sectionDAO;
        this.studentDAO = studentDAO;
    }

    @Override
    public void run(String... args) {
        try {
            courseDAO.createTable();
            enrollmentDAO.createTable();
            gradeDAO.createTable();
            sectionDAO.createTable();
            studentDAO.createTable();

          


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
