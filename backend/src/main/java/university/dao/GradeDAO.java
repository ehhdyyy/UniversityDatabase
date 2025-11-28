package university.dao;
import java.sql.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import university.model.Grade;

@Repository
public class GradeDAO {
    
    private JdbcTemplate jdbc;

    public GradeDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void createTable() throws SQLException {
        String sql = """
                DROP TABLE IF EXISTS grades CASCADE;
                CREATE TABLE grades(
                    gradeID SERIAL PRIMARY KEY,
                    enrollmentID INT NOT NULL REFERENCES enrollments(enrollmentID) ON DELETE CASCADE,
                    grade VARCHAR(2) NOT NULL
                );
                """;
        jdbc.execute(sql);
    }

    public void populateGrades() throws SQLException {
        String sql = """
                INSERT INTO grades(enrollmentID, grade) VALUES
                    (1, 'A'),
                    (2, 'B+'),
                    (3, 'A-'),
                    (4, 'C'),
                    (5, 'B'),
                    (6, 'A'),
                    (7, 'B-'),
                    (8, 'C+'),
                    (9, 'A'),
                    (10, 'B'),
                    (11, 'A-'),
                    (12, 'C'),
                    (13, 'B+'),
                    (14, 'A'),
                    (15, 'B-')
                    ;
                """;
        jdbc.execute(sql);
    }

    public List<String> findAllGrades(){
        return jdbc.query("""
            SELECT g.gradeID, g.enrollmentID, g.grade, s.lastName, s.firstName
            FROM grades g 
            JOIN enrollments e ON g.enrollmentID = e.enrollmentID 
            JOIN students s ON e.studentID = s.studentID
            ORDER BY gradeID
            """
        , (rs, rowNum) -> rs.getString("gradeID") + " " + rs.getString("enrollmentID") + " " + rs.getString("grade") + " " + rs.getString("lastName") + " " + rs.getString("firstName"));
    }

    public int insert(Grade grade) throws Exception{
        String sql = """
                    INSERT INTO grades(enrollmentID, grade) 
                    VALUES(?,?) 
                    RETURNING gradeID
                    """;

        Integer id = jdbc.queryForObject(
            sql,
            Integer.class,
            grade.getEnrollmentID(),
            grade.getGrade()
        );
        if (id == null){
            throw new SQLException("INSERT failed, no ID obtained.");
        }
        grade.setGradeID(id);
        return id;
    }

    public Grade findByID(int gradeID) throws Exception {
        String sql = """
                    SELECT gradeID, enrollmentID, grade 
                    FROM grades 
                    WHERE gradeID = ?
                    """;

        try {
            return jdbc.queryForObject(
                sql,
                (rs, rowNum) -> new Grade(
                    rs.getInt("gradeID"),
                    rs.getInt("enrollmentID"),
                    rs.getString("grade")
                ),
                gradeID
            );
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int update(Grade grade) throws Exception{
        String sql = """
                    UPDATE grades 
                    SET enrollmentID = ?, grade = ? 
                    WHERE gradeID = ? 
                    RETURNING gradeID
                    """;

        Integer id = jdbc.queryForObject(
            sql,
            Integer.class,
            grade.getEnrollmentID(),
            grade.getGrade(),
            grade.getGradeID()
        );
        
        if (id == null){
            throw new SQLException("UPDATE failed, no ID obtained.");
        }
        return id;
    }

    public List<Grade> findAll() throws SQLException {
        String sql = "SELECT gradeID, enrollmentID, grade FROM grades ORDER BY gradeID";
        
        return jdbc.query(
            sql,
            (rs, rowNum) -> new Grade(
                rs.getInt("gradeID"),
                rs.getInt("enrollmentID"),
                rs.getString("grade")
            )
        );
    }

    public void delete(int gradeID) throws SQLException {
        String sql = "DELETE FROM grades WHERE gradeID = ?";
        
        int rowsAffected = jdbc.update(sql, gradeID);
        if (rowsAffected == 0) {
            throw new SQLException("Delete failed, no grade with ID: " + gradeID);
        }
    }

}
