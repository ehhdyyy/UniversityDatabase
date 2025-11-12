package university.dao;
import java.sql.*;
import java.util.*;
import university.model.Student;

public class StudentDAO {
    
    private Connection conn;

    public StudentDAO(Connection conn) {
        this.conn = conn;
    }

    public void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS students(
                    studentID SERIAL PRIMARY KEY,
                    lastName VARCHAR(50) NOT NULL,
                    firstName VARCHAR(50) NOT NULL,
                    major VARCHAR(50) NOT NULL
                );
                """;
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    public int insert(Student student) throws Exception{

        String sql = "INSERT INTO students(lastName, firstName, major) VALUES(?,?,?) RETURNING studentID";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, student.getLastName());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getMajor());

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }else{
                    throw new SQLException("INSERT failed, no ID obtained.");
                }
            }
        }
    }

    public int update(Student student) throws Exception{

        String sql = "UPDATE students SET lastName = ?, firstName = ?, major = ? WHERE studentID = ? RETURNING studentID";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, student.getLastName());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getMajor());

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int generatedID = rs.getInt(1);
                    student.setStudentID(generatedID);
                    return generatedID;
                }else{
                    throw new SQLException("UPDATE failed, no ID obtained.");
                }
            }
        }
    }

    public List<Student> findAll() throws SQLException {
        String sql = "SELECT * FROM students ORDER BY studentID";
        List<Student> students = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("studentID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("major")
                );
                students.add(student);
            }
        }
        return students;
    }

    public void delete(int studentID) throws SQLException {
        String sql = "DELETE FROM students WHERE studentID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentID);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Delete failed, no student with ID: " + studentID);
            }
        }
    }
}
