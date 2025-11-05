package university.util;
import java.sql.*;
import java.util.*;
import java.io.*;

public class DataSourceFactory {
    public static Connection getConnection() throws Exception {
        Properties p = new Properties();
        try (InputStream fis = DataSourceFactory.class.getClassLoader().getResourceAsStream("applications.properties")){
            p.load(fis);
        }
        String url = p.getProperty("jdbc.url");
        String user = p.getProperty("jdbc.username");
        String password = p.getProperty("jdbc.password");
        return DriverManager.getConnection(url,user,password);
    }
}
