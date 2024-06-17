import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DatabaseUtils {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/chattingroom";
    private static final String USER = "root";  // database username
    private static final String PASS = "Ren2000**";  // Database Password

    // Static block for loading database drivers
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found.");
            e.printStackTrace();
        }
    }

    // Getting a database connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // Registered Users
    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO ID (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error executing registerUser in DatabaseUtils.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkUser(String username, String password) {
        String sql = "SELECT * FROM ID WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // If the query yields results, return true, indicating that the username and password match.
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
