package university.util;

import java.sql.*;
import university.dao.*;

public class DatabaseInitializer {

    public static void initialize(Connection conn) throws Exception {
        new CourseDAO(conn).createTable();
    }
    
}
