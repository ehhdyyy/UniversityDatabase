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
                CREATE TABLE IF NOT EXISTS sections(
                    sectionID SERIAL PRIMARY KEY,
                    courseID INT NOT NULL REFERENCES courses(courseID) ON DELETE CASCADE,
                    day_time VARCHAR(50) NOT NULL,
                    term VARCHAR(20) NOT NULL
                );
                """;
        jdbc.execute(sql);
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
