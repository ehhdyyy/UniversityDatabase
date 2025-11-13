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
                CREATE TABLE IF NOT EXISTS grades(
                    gradeID SERIAL PRIMARY KEY,
                    enrollmentID INT NOT NULL REFERENCES enrollments(enrollmentID) ON DELETE CASCADE,
                    grade VARCHAR(2) NOT NULL
                );
                """;
        jdbc.execute(sql);
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
