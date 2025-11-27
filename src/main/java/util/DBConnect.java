package  util;

import java.sql.*;


public class DBConnect {
    public static Connection getConnection() {
        Connection conn = null;

        try {
            String dbURL = "jdbc:mysql://localhost:3306/hms";
            String userName = "root";
            String password = "";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return null;
            }

            conn = DriverManager.getConnection(dbURL, userName, password);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }

        return conn;
    }

}
