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
            System.out.println("Before Insert");
            int cid = courses.insert(new Course("Introduction to Database Management Systems", "dbms!"));
            System.out.println("Inserted course id=" + cid);
        }
        System.out.println("END main");
    }
}
