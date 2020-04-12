package utilities;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    private static Connection conn = null;
    private static Statement stmt = null;
    //private String dbUser = null;
    //private String dbPass = null;

    public static Connection startConnection() {

        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://3.227.166.251/U071A3";

        //  Database credentials
        final String DBUSER = "U071A3";
        final String DBPASS = "53688930947";
        final String DBNAME = "U071A3";

        boolean res = false;

        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        try {
            conn.close();
            System.out.println("Connection closed.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection(){
        return conn;
    }
}
