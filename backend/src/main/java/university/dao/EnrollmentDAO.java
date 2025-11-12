package university.dao;
import java.sql.*;
import java.util.*;
import university.model.Enrollment;

public class EnrollmentDAO {
    
    private Connection conn;

    public EnrollmentDAO(Connection conn) {
        this.conn = conn;
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
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    public int insert(Enrollment enrollment) throws Exception{
        String sql = "INSERT INTO enrollments(studentID, sectionID) VALUES(?,?) RETURNING enrollmentID";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, enrollment.getStudentID());
            ps.setInt(2, enrollment.getSectionID());

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int generatedID = rs.getInt(1);
                    enrollment.setEnrollmentID(generatedID);
                    return generatedID;
                }else{
                    throw new SQLException("INSERT failed, no ID obtained.");
                }
            }
        }
    }

    public Enrollment findByID(int enrollmentID) throws Exception {
        String sql = "SELECT enrollmentID, studentID, sectionID FROM enrollments WHERE enrollmentID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, enrollmentID);

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Enrollment(
                        rs.getInt("enrollmentID"),
                        rs.getInt("studentID"),
                        rs.getInt("sectionID")
                    );
                }
                else{
                    throw new SQLException("No enrollment found with ID:" + enrollmentID);
                }
            }
        }
    }

    public int update(Enrollment enrollment) throws Exception{
        String sql = "UPDATE enrollments SET studentID = ?, sectionID = ? WHERE enrollmentID = ? RETURNING enrollmentID";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, enrollment.getStudentID());
            ps.setInt(2, enrollment.getSectionID());
            ps.setInt(3, enrollment.getEnrollmentID());

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }else{
                    throw new SQLException("UPDATE failed, no ID obtained.");
                }
            }
        }
    }
}
