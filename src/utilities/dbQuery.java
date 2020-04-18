package utilities;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dbQuery {
@FXML
//    public static List<String> getAllCustomers() throws SQLException {
//        List<String> allCustomers = new ArrayList<String>();
//        PreparedStatement pstmt = DBConnection.getConnection().prepareStatement("SELECT * FROM U071A3.customer");
//        try {
//            ResultSet rs = pstmt.e
//                    executeQuery();
//
//            while (rs.next()) {
//                String current = rs.getString("customerName");
//                allCustomers.add(current);
//            }
//        }
//        catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        finally {
//            if (pstmt != null) pstmt.close();
//        }
//        return allCustomers;
//    }


    private static String queryToExecute;
    private static Statement stmt;
    private static ResultSet result;
    private static Connection conn = DBConnection.getConnection();

    public static void createQuery(String q) {
//        query = q;

        try {
            //create Statement object
            stmt = conn.createStatement();

            //determine query execution
            if(q.toLowerCase().startsWith("select"))
                result = stmt.executeQuery(q);
            if(q.toLowerCase().startsWith("delete") || q.toLowerCase().startsWith("update") || q.toLowerCase().startsWith("insert"))
                stmt.executeUpdate(q);
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static ResultSet getQueryResultSet() {
        return result;
    }

}
