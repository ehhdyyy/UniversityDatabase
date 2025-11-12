package university.dao;
import java.sql.*;
import java.util.*;
import university.model.Grade;


public class GradeDAO {
    
    private Connection conn;

    public GradeDAO(Connection conn) {
        this.conn = conn;
    }

    public void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS grades(
                    gradeID SERIAL PRIMARY KEY,
                    enrollmentID INT NOT NULL REFERENCES enrollments(enrollmentID) ON DELETE CASCADE,
                    grade VARCHAR(2) NOT NULL
                );
                """;
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    public int insert(Grade grade) throws Exception{

        String sql = "INSERT INTO grades(enrollmentID, grade) VALUES(?,?) RETURNING gradeID";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, grade.getEnrollmentID());
            ps.setString(2, grade.getGrade());

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int generatedID = rs.getInt(1);
                    grade.setGradeID(generatedID);
                    return generatedID;
                }else{
                    throw new SQLException("INSERT failed, no ID obtained.");
                }
            }
        }
    }

    public Grade findByID(int gradeID) throws Exception {
        String sql = "SELECT gradeID, enrollmentID, grade FROM grades WHERE gradeID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, gradeID);

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Grade(
                        rs.getInt("gradeID"),
                        rs.getInt("enrollmentID"),
                        rs.getString("grade")
                    );
                }
                else{
                    throw new SQLException("No grade found with ID: " + gradeID);
                }
            }
        }
    }

    public int update(Grade grade) throws Exception{
        String sql = "UPDATE grades SET enrollmentID = ?, grade = ? WHERE gradeID = ? RETURNING gradeID";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, grade.getEnrollmentID());
            ps.setString(2, grade.getGrade());
            ps.setInt(3, grade.getGradeID());

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
