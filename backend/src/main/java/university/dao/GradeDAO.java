package university.dao;
import java.sql.*;
import java.util.*;

import university.model.Enrollment;
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
                    return rs.getInt(1);
                }else{
                    throw new SQLException("INSERT failed, no ID obtained.");
                }
            }
        }
    }
}
