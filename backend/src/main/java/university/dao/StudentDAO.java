package university.dao;
import java.sql.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import university.model.Student;

@Repository
public class StudentDAO {
    
    private JdbcTemplate jdbc;

    public StudentDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
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
        jdbc.execute(sql);
    }

    public List<String> findAllNames() {
        return jdbc.query("SELECT studentID, lastname, firstname FROM students ORDER BY studentID", (rs, rowNum) -> rs.getString("studentID") + " " + rs.getString("lastName") + " " + rs.getString("firstName"));
    }

    public int insert(Student student) throws Exception{

        String sql = """
                    INSERT INTO students(lastName, firstName, major) 
                    VALUES(?,?,?) 
                    RETURNING studentID;
                    """;

        Integer id = jdbc.queryForObject(
            sql,
            Integer.class,
            student.getLastName(),
            student.getFirstName(),
            student.getMajor()
        );
        if (id == null){
            throw new SQLException("INSERT failed, no ID obtained.");
        }
        student.setStudentID(id);
        return id;
    }

    public Student findByID(int studentID) throws Exception {
        String sql = """
                    SELECT studentID, lastName, firstName, major 
                    FROM students 
                    WHERE studentID = ?
                    """;

        try {
            return jdbc.queryForObject(
                sql,
                (rs, rowNum) -> new Student(
                    rs.getInt("studentID"),
                    rs.getString("lastName"),
                    rs.getString("firstName"),
                    rs.getString("major")
                ),
                studentID
            );
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int update(Student student) throws Exception{

        String sql = """
                    UPDATE students 
                    SET lastName = ?, firstName = ?, major = ? 
                    WHERE studentID = ? 
                    RETURNING studentID
                    """;

        Integer id = jdbc.queryForObject(
            sql, 
            Integer.class,
            student.getLastName(),
            student.getFirstName(),
            student.getMajor(),
            student.getStudentID()
        );
        
        if (id == null){
            throw new SQLException("INSERT failed, no ID obtained.");
        }
        student.setStudentID(id);
        return id;
    }
}
