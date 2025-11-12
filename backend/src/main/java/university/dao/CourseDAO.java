package university.dao;
import java.sql.*;
import java.util.*;
import university.model.Course;
import university.model.Student;

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
                    description TEXT NOT NULL
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
                    int generatedID = rs.getInt(1);
                    course.setCourseID(generatedID);
                    System.out.println("Course ID set to: " + generatedID);
                    return generatedID;
                }else{
                    throw new SQLException("INSERT failed, no ID obtained.");
                }
            }
        }
    }

    public Course findByID(int courseID) throws Exception {
        String sql = "SELECT courseID, courseName, description FROM courses WHERE courseID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseID);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new Course(
                        rs.getInt("courseID"),
                        rs.getString("courseName"),
                        rs.getString("description")
                    );
                }
                else{
                    throw new SQLException("No course found with ID: " + courseID);
                }
            }
        }
    }

    public int update(Course course) throws Exception{
        String sql = "UPDATE courses SET courseName = ?, description = ? WHERE courseID = ? RETURNING courseID";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDescription());
            ps.setInt(3, course.getCourseID());

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }else{
                    throw new SQLException("UPDATE failed, no ID obtained.");
                }
            }
        }
    }
}
