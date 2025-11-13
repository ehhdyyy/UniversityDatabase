package university.dao;
import java.sql.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import university.model.Enrollment;

@Repository
public class EnrollmentDAO {
    
    private JdbcTemplate jdbc;

    public EnrollmentDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS enrollments(
                    enrollmentID SERIAL PRIMARY KEY,
                    studentID INT NOT NULL REFERENCES students(studentID) ON DELETE CASCADE,
                    sectionID INT NOT NULL REFERENCES sections(sectionID) ON DELETE CASCADE,
                    UNIQUE(studentID, sectionID)
                );
                """;
        jdbc.execute(sql);
    }

    public int insert(Enrollment enrollment) throws Exception{

        String sql = """
                    INSERT INTO enrollments(studentID, sectionID) 
                    VALUES(?,?) 
                    RETURNING enrollmentID
                    """;

        Integer id = jdbc.queryForObject(
            sql,
            Integer.class,
            enrollment.getStudentID(),
            enrollment.getSectionID()
        );
        if (id == null){
            throw new SQLException("INSERT failed, no ID obtained.");
        }
        enrollment.setEnrollmentID(id);
        return id;
    }

    public Enrollment findByID(int enrollmentID) throws Exception {

        String sql = """
                    SELECT enrollmentID, studentID, sectionID 
                    FROM enrollments 
                    WHERE enrollmentID = ?
                    """;

        try {
            return jdbc.queryForObject(
                sql,
                (rs, rowNum) -> new Enrollment(
                    rs.getInt("enrollmentID"),
                    rs.getInt("studentID"),
                    rs.getInt("sectionID")
                ),
                enrollmentID
            );
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int update(Enrollment enrollment) throws Exception{

        String sql = """
                    UPDATE enrollments 
                    SET studentID = ?, sectionID = ? 
                    WHERE enrollmentID = ? RETURNING enrollmentID
                    """;
        
        Integer id = jdbc.queryForObject(
            sql,
            Integer.class,
            enrollment.getStudentID(),
            enrollment.getSectionID(),
            enrollment.getEnrollmentID()
        );
        
        if (id == null){
            throw new SQLException("UPDATE failed, no ID obtained.");
        }
        enrollment.setEnrollmentID(id);
        return id;
    }

    public List<Enrollment> findAll() throws SQLException {
        String sql = "SELECT enrollmentID, studentID, sectionID FROM enrollments ORDER BY enrollmentID";
        
        return jdbc.query(
            sql,
            (rs, rowNum) -> new Enrollment(
                rs.getInt("enrollmentID"),
                rs.getInt("studentID"),
                rs.getInt("sectionID")
            )
        );
    }

    public void delete(int enrollmentID) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE enrollmentID = ?";
        
        int rowsAffected = jdbc.update(sql, enrollmentID);
        if (rowsAffected == 0) {
            throw new SQLException("Delete failed, no enrollment with ID: " + enrollmentID);
        }
    }
}
