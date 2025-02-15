package databasePart1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import application.User;

/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, invitation codes, etc.
 */
public class DatabaseHelper {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/FoundationDatabase";

    // Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    private Connection connection = null;
    private Statement statement = null;

    /**
     * Connects to the H2 database, creates tables if not present.
     */
    public void connectToDatabase() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();

            createTables();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
    }

    /**
     * Creates necessary tables (users, invitation codes) if they don't exist.
     */
    private void createTables() throws SQLException {
        String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "userName VARCHAR(255) UNIQUE, "
                + "password VARCHAR(255), "
                + "role VARCHAR(20))";
        statement.execute(userTable);

        String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
                + "code VARCHAR(10) PRIMARY KEY, "
                + "isUsed BOOLEAN DEFAULT FALSE)";
        statement.execute(invitationCodesTable);
    }

    /**
     * Checks if the database user table is empty (no users).
     */
    public boolean isDatabaseEmpty() throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM cse360users";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("count") == 0;
        }
        return true;
    }

    /**
     * Registers a new user in the database.
     */
    public void register(User user) throws SQLException {
        String insertUser = "INSERT INTO cse360users (userName, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.executeUpdate();
        }
    }

    /**
     * Validates a user's login credentials against the database.
     */
    public boolean login(User user) throws SQLException {
        String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Checks if a user already exists by userName.
     */
    public boolean doesUserExist(String userName) {
        String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // If the count is greater than 0, the user exists
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // If an error occurs, assume user doesn't exist
    }

    /**
     * Retrieves the role of a user from the database using their userName.
     */
    public String getUserRole(String userName) {
        String query = "SELECT role FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role"); // Return the role if user exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // If no user exists or an error occurs
    }

    /**
     * Generates a new invitation code and inserts it into the database.
     */
    public String generateInvitationCode() {
        String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
        String query = "INSERT INTO InvitationCodes (code) VALUES (?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * Validates an invitation code to check if it is unused.
     */
    public boolean validateInvitationCode(String code) {
        String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                markInvitationCodeAsUsed(code);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Marks the invitation code as used in the database.
     */
    private void markInvitationCodeAsUsed(String code) {
        String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the user's password to a new specified password.
     */
    public void resetUserPassword(String userName, String newPassword) throws SQLException {
        String updateSql = "UPDATE cse360users SET password=? WHERE userName=?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, userName);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a user from the database by their username.
     */
    public void deleteUser(String userName) throws SQLException {
        String deleteSql = "DELETE FROM cse360users WHERE userName=?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setString(1, userName);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all users in the database as a list of User objects.
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String selectSql = "SELECT userName, password, role FROM cse360users";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("userName");
                String pwd = rs.getString("password");
                String role = rs.getString("role");
                userList.add(new User(name, pwd, role));
            }
        }
        return userList;
    }

    /**
     * Updates the user's role in the database.
     */
    public void updateUserRole(String userName, String newRole) throws SQLException {
        String updateSql = "UPDATE cse360users SET role=? WHERE userName=?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
            pstmt.setString(1, newRole);
            pstmt.setString(2, userName);
            pstmt.executeUpdate();
        }
    }

    /**
     * Closes the database connection and statement.
     */
    public void closeConnection() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException se2) {
            se2.printStackTrace();
        }
        try {
            if (connection != null) connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
