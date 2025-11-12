package university.util;

import university.dao.*;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseInitializer {
    public static void initialize(Connection connection) throws Exception {
        
        new CourseDAO(connection).createTable();
        new StudentDAO(connection).createTable();
        
        new SectionDAO(connection).createTable();
        
        new EnrollmentDAO(connection).createTable();
        
        new GradeDAO(connection).createTable();
    }
}