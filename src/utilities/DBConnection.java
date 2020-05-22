package utilities;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class DBConnection {
    private static Connection conn = null;
    private static Statement stmt = null;

    public static void startConnection() {

        final String DB_URL = "jdbc:mysql://3.227.166.251/U071A3";
        //  Database credentials
        final String DBUSER = "U071A3";
        final String DBPASS = "53688930947";

        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void closeConnection() throws SQLException {
        try {
            conn.close();
            System.out.println("Connection closed.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static Connection getConnection(){
        return conn;
    }
}
