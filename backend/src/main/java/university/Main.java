package university;

import university.dao.*;
import university.model.*;
import university.util.DataSourceFactory;
import university.util.DatabaseInitializer;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("START main");

        try (Connection connection = DataSourceFactory.getConnection()) {
            System.out.println("Connection established.");

            DatabaseInitializer.initialize(connection);
            System.out.println("Tables created.");

            CourseDAO courseDAO = new CourseDAO(connection);
            SectionDAO sectionDAO = new SectionDAO(connection);

            System.out.println("\n=== INSERTING COURSES ===");
            Course course1 = new Course(0, "CS101", "Intro to Computer Science");
            Course course2 = new Course(0, "MATH201", "Calculus II");
            
            courseDAO.insert(course1);
            courseDAO.insert(course2);
            System.out.println("Inserted 2 courses.");

            System.out.println("\n=== INSERTING SECTIONS ===");
            Section section1 = new Section(0, course1.getCourseID(), "MWF 10:00-11:00", "Fall 2025");
            Section section2 = new Section(0, course2.getCourseID(), "TTh 14:00-15:30", "Fall 2025");
            
            sectionDAO.insert(section1);
            sectionDAO.insert(section2);
            System.out.println("Inserted 2 sections.");

            System.out.println("\n=== FINDING SECTIONS ===");
            // test for sections and courses initially inserted
            Section found1 = sectionDAO.findByID(section1.getSectionID());
            System.out.println("Found Section " + found1.getSectionID() + ": " + found1.getDayTime() + " (" + found1.getTerm() + ")");
            
            Section found2 = sectionDAO.findByID(section2.getSectionID());
            System.out.println("Found Section " + found2.getSectionID() + ": " + found2.getDayTime() + " (" + found2.getTerm() + ")");

            Course foundCourse = courseDAO.findByID(course1.getCourseID());
            System.out.println("Found Course " + foundCourse.getCourseID() + ": " + foundCourse.getCourseName());


        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nEND main");
    }
}