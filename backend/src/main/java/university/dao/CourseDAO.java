package university.dao;
import java.sql.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import university.model.Course;

@Repository
public class CourseDAO {

    private JdbcTemplate jdbc;

    public CourseDAO(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }
    
    public void createTable() throws Exception{
        String sql = """
                CREATE TABLE IF NOT EXISTS courses(
                    courseID SERIAL PRIMARY KEY,
                    courseName VARCHAR(100) NOT NULL,
                    description TEXT NOT NULL
                );
                """;
        
        jdbc.execute(sql);
    }

    public int insert(Course course) throws Exception{
        String sql = """
                    INSERT INTO courses(courseName, description) 
                    VALUES(?,?) 
                    RETURNING courseID
                    """;

        Integer id = jdbc.queryForObject(
            sql,
            Integer.class,
            course.getCourseName(),
            course.getDescription()
        );
        if (id == null){
            throw new SQLException("INSERT failed, no ID obtained.");
        }
        course.setCourseID(id);
        return id;
    }

    public Course findByID(int courseID) throws Exception {
        String sql = """
                    SELECT courseID, courseName, description 
                    FROM courses 
                    WHERE courseID = ?
                    """;

        try {
            return jdbc.queryForObject(
                sql,
                (rs, rowNum) -> new Course(
                    rs.getInt("courseID"),
                    rs.getString("courseName"),
                    rs.getString("description")
                ),
                courseID
            );
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Course> findAll() throws SQLException {
        String sql = "SELECT courseID, courseName, description FROM courses ORDER BY courseID";
        
        return jdbc.query(
            sql,
            (rs, rowNum) -> new Course(
                rs.getInt("courseID"),
                rs.getString("courseName"),
                rs.getString("description")
            )
        );
    }

    public void delete(int courseID) throws SQLException {
        String sql = "DELETE FROM courses WHERE courseID = ?";
        
        int rowsAffected = jdbc.update(sql, courseID);
        if (rowsAffected == 0) {
            throw new SQLException("Delete failed, no course with ID: " + courseID);
        }
    }

    public int update(Course course) throws Exception{
        String sql = """
                    UPDATE courses 
                    SET courseName = ?, description = ? 
                    WHERE courseID = ? 
                    RETURNING courseID
                    """;

        Integer id = jdbc.queryForObject(
            sql,
            Integer.class,
            course.getCourseName(),
            course.getDescription(),
            course.getCourseID()
        );
        
        if (id == null){
            throw new SQLException("UPDATE failed, no ID obtained.");
        }
        return id;
    }
}
