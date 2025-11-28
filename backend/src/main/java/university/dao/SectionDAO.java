package university.dao;
import java.sql.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import university.model.Section;

@Repository
public class SectionDAO {
    
    private JdbcTemplate jdbc;

    public SectionDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void createTable() throws SQLException {
        String sql = """
                DROP TABLE IF EXISTS sections CASCADE;
                CREATE TABLE sections(
                    sectionID SERIAL PRIMARY KEY,
                    courseID INT NOT NULL REFERENCES courses(courseID) ON DELETE CASCADE,
                    day_time VARCHAR(50) NOT NULL,
                    term VARCHAR(20) NOT NULL
                );
                """;
        jdbc.execute(sql);
    }

    public void populateSections() throws SQLException {
        String sql = """
                INSERT INTO sections(courseID, day_time, term) VALUES
                    (1, 'Mon-Wed 10:00-11:30', 'Fall 2024'),
                    (2, 'Tue-Thu 12:00-13:30', 'Fall 2024'),
                    (3, 'Mon-Wed 14:00-15:30', 'Spring 2025'),
                    (4, 'Tue-Thu 09:00-10:30', 'Spring 2025'),
                    (5, 'Mon-Wed 11:00-12:30', 'Fall 2024'),
                    (6, 'Tue-Thu 13:00-14:30', 'Fall 2024'),
                    (7, 'Mon-Wed 15:00-16:30', 'Spring 2025'),
                    (8, 'Tue-Thu 10:00-11:30', 'Spring 2025'),
                    (9, 'Mon-Wed 08:00-09:30', 'Fall 2024'),
                    (10, 'Tue-Thu 15:00-16:30', 'Fall 2024'),
                    (11, 'Mon-Wed 12:00-13:30', 'Spring 2025'),
                    (12, 'Tue-Thu 14:00-15:30', 'Spring 2025'),
                    (13, 'Mon-Wed 09:00-10:30', 'Fall 2024'),
                    (14, 'Tue-Thu 11:00-12:30', 'Fall 2024'),
                    (15, 'Mon-Wed 16:00-17:30', 'Spring 2025')
                    ;
                """;
        jdbc.execute(sql);
    }

    public List<String> findAllSections() {
        return jdbc.query("""
                    SELECT sec.sectionID, sec.courseID, c.courseName, sec.day_time, sec.term
                    FROM sections sec
                    JOIN courses c ON sec.courseID = c.courseID
                    ORDER BY sec.sectionID;
                """
                        
                , (rs, rowNum) -> rs.getString("sectionID") + " " + rs.getString("courseID") + " " + rs.getString("courseName") + " " + rs.getString("day_time") + " " + rs.getString("term"));
    }

    public int insert(Section section) throws Exception{
        String sql = """
                    INSERT INTO sections(courseID, day_time, term) 
                    VALUES(?,?,?) 
                    RETURNING sectionID
                    """;

        Integer id = jdbc.queryForObject(
            sql,
            Integer.class,
            section.getCourseID(),
            section.getDayTime(),
            section.getTerm()
        );
        if (id == null){
            throw new SQLException("INSERT failed, no ID obtained.");
        }
        section.setSectionID(id);
        return id;
    }

    public Section findByID(int sectionID) throws Exception {
        String sql = """
                    SELECT sectionID, courseID, day_time, term 
                    FROM sections 
                    WHERE sectionID = ?
                    """;

        try {
            return jdbc.queryForObject(
                sql,
                (rs, rowNum) -> new Section(
                    rs.getInt("sectionID"),
                    rs.getInt("courseID"),
                    rs.getString("day_time"),
                    rs.getString("term")
                ),
                sectionID
            );
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int update(Section section) throws Exception{
        String sql = """
                    UPDATE sections 
                    SET courseID = ?, day_time = ?, term = ? 
                    WHERE sectionID = ? 
                    RETURNING sectionID
                    """;

        Integer id = jdbc.queryForObject(
            sql,
            Integer.class,
            section.getCourseID(),
            section.getDayTime(),
            section.getTerm(),
            section.getSectionID()
        );
        
        if (id == null){
            throw new SQLException("UPDATE failed, no ID obtained.");
        }
        return id;
    }

    public List<Section> findAll() throws SQLException {
        String sql = "SELECT sectionID, courseID, day_time, term FROM sections ORDER BY sectionID";
        
        return jdbc.query(
            sql,
            (rs, rowNum) -> new Section(
                rs.getInt("sectionID"),
                rs.getInt("courseID"),
                rs.getString("day_time"),
                rs.getString("term")
            )
        );
    }

    public void delete(int sectionID) throws SQLException {
        String sql = "DELETE FROM sections WHERE sectionID = ?";
        
        int rowsAffected = jdbc.update(sql, sectionID);
        if (rowsAffected == 0) {
            throw new SQLException("Delete failed, no section with ID: " + sectionID);
        }
    }

}
