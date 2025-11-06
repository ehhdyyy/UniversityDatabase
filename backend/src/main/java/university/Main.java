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

            System.out.println("Before Insert");

            int cid = courses.insert(new Course("Data Structures and Algorithms", "dsa!"));
            System.out.println("Inserted course id=" + cid);

            int sid = students.insert(new Student("Yan", "Jason", "Data Science"));
            System.out.println("Inserted student id=" + sid);

            int eid = enrollment.insert(new Enrollment(1, 10));
            System.out.println("Inserted enrollment id=" + eid);
        }
        System.out.println("END main");
    }
}
