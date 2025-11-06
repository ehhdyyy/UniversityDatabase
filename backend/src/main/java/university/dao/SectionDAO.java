package university.dao;
import java.sql.*;
import java.util.*;
import university.model.Section;

public class SectionDAO {
    
    private Connection conn;

    public SectionDAO(Connection conn) {
        this.conn = conn;
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
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    public int insert(Section section) throws Exception{

        String sql = "INSERT INTO section(courseID, day_time, term) VALUES(?,?,?) RETURNING sectionID";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, section.getCourseID());
            ps.setString(2, section.getDayTime());
            ps.setString(3, section.getTerm());

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
