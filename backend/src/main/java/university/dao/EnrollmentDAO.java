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
                DROP TABLE IF EXISTS enrollments CASCADE;
                CREATE TABLE enrollments(
                    enrollmentID SERIAL PRIMARY KEY,
                    studentID INT NOT NULL REFERENCES students(studentID) ON DELETE CASCADE,
                    sectionID INT NOT NULL REFERENCES sections(sectionID) ON DELETE CASCADE,
                    UNIQUE(studentID, sectionID)
                );
                """;
        jdbc.execute(sql);
    }

    public void populateEnrollments() throws SQLException {
        String sql = """
                INSERT INTO enrollments(studentID, sectionID) VALUES
                    (1, 1),
                    (2, 1),
                    (3, 2),
                    (4, 2),
                    (5, 3),
                    (6, 3),
                    (7, 4),
                    (8, 4),
                    (9, 5),
                    (10, 5),
                    (11, 6),
                    (12, 6),
                    (13, 7),
                    (14, 7),
                    (15, 8)
                    ;
                """;
        jdbc.execute(sql);
    }

    public void newEnrollmentGradeTrigger() throws SQLException {
        String sql = """
                CREATE OR REPLACE FUNCTION newEnrollmentGradeTrigger()
                RETURNS TRIGGER AS $$
                BEGIN
                    INSERT INTO grades(enrollmentID, grade) VALUES(NEW.enrollmentID, 'NA');
                    RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                
                CREATE TRIGGER after_enrollment_insert
                AFTER INSERT ON enrollments
                FOR EACH ROW EXECUTE FUNCTION newEnrollmentGradeTrigger();
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

    public List<Enrollment> findAllWithDetails() throws SQLException {
        String sql = """
                SELECT e.enrollmentID, e.studentID, e.sectionID,
                       s.firstName || ' ' || s.lastName as studentName,
                       c.courseName, sec.day_time, sec.term
                FROM enrollments e
                JOIN students s ON e.studentID = s.studentID
                JOIN sections sec ON e.sectionID = sec.sectionID
                JOIN courses c ON sec.courseID = c.courseID
                ORDER BY e.enrollmentID
                """;
        
        return jdbc.query(sql, (rs, rowNum) -> {
            Enrollment enrollment = new Enrollment(
                rs.getInt("enrollmentID"),
                rs.getInt("studentID"),
                rs.getInt("sectionID")
            );
            enrollment.setStudentName(rs.getString("studentName"));
            enrollment.setCourseName(rs.getString("courseName"));
            enrollment.setDayTime(rs.getString("day_time"));
            enrollment.setTerm(rs.getString("term"));
            return enrollment;
        });
    }

    public List<Enrollment> filterByStudentId(int studentId) throws SQLException {
        String sql = """
                SELECT e.enrollmentID, e.studentID, e.sectionID,
                       s.firstName || ' ' || s.lastName as studentName,
                       c.courseName, sec.day_time, sec.term
                FROM enrollments e
                JOIN students s ON e.studentID = s.studentID
                JOIN sections sec ON e.sectionID = sec.sectionID
                JOIN courses c ON sec.courseID = c.courseID
                WHERE e.studentID = ?
                ORDER BY e.enrollmentID
                """;
        
        return jdbc.query(sql, (rs, rowNum) -> {
            Enrollment enrollment = new Enrollment(
                rs.getInt("enrollmentID"),
                rs.getInt("studentID"),
                rs.getInt("sectionID")
            );
            enrollment.setStudentName(rs.getString("studentName"));
            enrollment.setCourseName(rs.getString("courseName"));
            enrollment.setDayTime(rs.getString("day_time"));
            enrollment.setTerm(rs.getString("term"));
            return enrollment;
        }, studentId);
    }

    public List<Enrollment> filterBySectionId(int sectionId) throws SQLException {
        String sql = """
                SELECT e.enrollmentID, e.studentID, e.sectionID,
                       s.firstName || ' ' || s.lastName as studentName,
                       c.courseName, sec.day_time, sec.term
                FROM enrollments e
                JOIN students s ON e.studentID = s.studentID
                JOIN sections sec ON e.sectionID = sec.sectionID
                JOIN courses c ON sec.courseID = c.courseID
                WHERE e.sectionID = ?
                ORDER BY e.enrollmentID
                """;
        
        return jdbc.query(sql, (rs, rowNum) -> {
            Enrollment enrollment = new Enrollment(
                rs.getInt("enrollmentID"),
                rs.getInt("studentID"),
                rs.getInt("sectionID")
            );
            enrollment.setStudentName(rs.getString("studentName"));
            enrollment.setCourseName(rs.getString("courseName"));
            enrollment.setDayTime(rs.getString("day_time"));
            enrollment.setTerm(rs.getString("term"));
            return enrollment;
        }, sectionId);
    }
}
