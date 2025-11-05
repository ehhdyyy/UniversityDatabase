package university.dao;
import java.sql.*;
import java.util.*;
import university.model.Course;

public class CourseDAO {

    private Connection conn;

    public CourseDAO(Connection c){
        this.conn = c;
    }
    
    public void createTable() throws Exception{
        String sql = """
                CREATE TABLE IF NOT EXISTS courses(
                    courseID SERIAL PRIMARY KEY,
                    courseName VARCHAR(100) NOT NULL,
                    description TEXT
                );
                """;
        try (Statement st = conn.createStatement()){
            st.execute(sql);
        }
    }

    public int insert(Course course) throws Exception{

        String sql = "INSERT INTO courses(courseName, description) VALUES(?,?) RETURNING courseID";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDescription());

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }else{
                    throw new SQLException("INSERT failed, no ID obtained.");
                }
            }
        }
    }
}
