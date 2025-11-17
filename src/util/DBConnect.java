package util;

import java.sql.*;

/*
            String dbURL = "jdbc:mysql://localhost:3306/hms";
            String userName = "localhost:3306/hms";
            String password = "java/hms";
 */
public class DBConnect {
    public static Connection getConnection() {
        Connection conn = null;

        try {
            String dbURL = "jdbc:mysql://localhost:3306/hms";
            String userName = "localhost:3306/hms";
            String password = "java/hms";
            conn = DriverManager.getConnection(dbURL, userName, password);
        } catch (SQLException e){
            return null;
        }

        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            if(conn!=null){
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
