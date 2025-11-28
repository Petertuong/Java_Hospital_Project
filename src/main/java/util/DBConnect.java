package  util;

import java.sql.*;
import java.io.*;
import java.nio.file.*;


public class DBConnect {
    public static Connection getConnection() {
        Connection conn = null;

        try {
            java.util.Map<String,String> env = System.getenv();
            String dbURL = env.getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/hms");
            String userName = env.getOrDefault("DB_USER", "root");
            String password = env.getOrDefault("DB_PASS", "");

            // Defensive: trim values to avoid accidental leading/trailing whitespace
            if (dbURL != null) dbURL = dbURL.trim();
            if (userName != null) userName = userName.trim();
            if (password != null) password = password.trim();

            // Debug: print resolved env values (mask password) to help diagnose auth issues
            String maskedPass = (password == null || password.isEmpty()) ? "<empty>" : "********";
            System.out.println("[DBConnect] DB_URL='" + dbURL + "' DB_USER='" + userName + "' DB_PASS='" + maskedPass + "'");

            // Also append to a dedicated log file for CI/remote runs where console may not show UI output
            try {
                Path logsDir = Paths.get("logs");
                if (!Files.exists(logsDir)) {
                    Files.createDirectories(logsDir);
                }
                try (PrintWriter pw = new PrintWriter(new FileWriter(logsDir.resolve("dbconnect.log").toFile(), true))) {
                    pw.println(java.time.ZonedDateTime.now() + " [DBConnect] DB_URL='" + dbURL + "' DB_USER='" + userName + "' DB_PASS='" + maskedPass + "'");
                }
            } catch (IOException ioe) {
                // If file logging fails, print stack to console but continue
                ioe.printStackTrace();
            }

            // Ensure the MySQL JDBC driver is loaded (helps when classpath/module issues occur)
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException cnfe) {
                System.out.println("[DBConnect] MySQL JDBC driver class not found on classpath: " + cnfe.getMessage());
            }

            conn = DriverManager.getConnection(dbURL, userName, password);
        } catch (SQLException e){
            // Print exception so failures are visible in logs / console
            e.printStackTrace();
            return null;
        }

        return conn;
    }

}
