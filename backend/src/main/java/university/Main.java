package university;
import university.dao.*;
import university.model.*;
import university.util.*;
import java.sql.*;

public class Main {
    
    public static void main(String[] args) throws Exception{
        System.out.println("START main");
        try (Connection conn = DataSourceFactory.getConnection()){
            System.out.println("Connection established.");
            DatabaseInitializer.initialize(conn);
            var courses = new CourseDAO(conn);
            var enrollment = new EnrollmentDAO(conn);
            var students = new StudentDAO(conn);
            var sections = new SectionDAO(conn);
            var grades = new GradeDAO(conn);


            Section s = sections.findByID(1);
            s.setDayTime("MWF 10:00-11:30");
            s.setTerm("Spring 2026");
            sections.update(s);
            System.out.println("Updated Section: " + s.getSectionID());
        
        }
        System.out.println("END main");
    }
}
