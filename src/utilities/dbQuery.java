package utilities;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dbQuery {
    private static Statement selectStmt, duiStmt;
    private static ResultSet result;
    private static int numRowsAffected;
    private static Connection conn = DBConnection.getConnection();

    public static void createQuery(String q) {
        try {
                selectStmt = conn.createStatement();
                duiStmt = conn.createStatement();
            //determine query execution
            if(q.toLowerCase().startsWith("select"))
                //create Statement object for select
                result = selectStmt.executeQuery(q);
            if(q.toLowerCase().startsWith("delete") || q.toLowerCase().startsWith("update") || q.toLowerCase().startsWith("insert"))
                //create Statement object for delete, update, insert
                numRowsAffected = duiStmt.executeUpdate(q, Statement.RETURN_GENERATED_KEYS);
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static ResultSet getQueryResultSet() {
        return result;
    }
    // For delete, update, insert check number of rows affected to see if query was successful
    public static int queryNumRowsAffected() {
        return numRowsAffected;
    }

    public static int getInsertedRowId() throws SQLException {
        try (ResultSet generatedKeys = duiStmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Executing query failed, no ID obtained.");
            }
        }
    }
}
