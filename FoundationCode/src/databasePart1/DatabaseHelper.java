package databasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

import application.User;
import application.UserRole;
import questions.Answer;
import questions.Question;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:./src/FoundationCode/databasePart1";

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			// statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "role VARCHAR(20))";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "isUsed BOOLEAN DEFAULT FALSE)";
	    statement.execute(invitationCodesTable);
	    
	    String createQuestionsTable = "CREATE TABLE IF NOT EXISTS questions ("
	            + "uuid VARCHAR(36) PRIMARY KEY, " 
	            + "title VARCHAR(255) NOT NULL, "
	            + "body_text TEXT NOT NULL, "
	            + "author VARCHAR(255) NOT NULL, "
	            + "posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
	            + "edited_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
	            + "likes INT DEFAULT 0, "
	            + "resolved BOOLEAN DEFAULT FALSE,"
	            + "resolving_child_uuid VARCHAR(36),"
	            + "child_uuids TEXT DEFAULT '')"; // Store children UUIDs as comma-separated values
	    statement.execute(createQuestionsTable);


	    String createAnswersTable = "CREATE TABLE IF NOT EXISTS answers ("
	            + "uuid VARCHAR(36) PRIMARY KEY, " 
	            + "body_text TEXT NOT NULL, "
	            + "author VARCHAR(255) NOT NULL, "
	            + "posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
	            + "edited_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
	            + "likes INT DEFAULT 0, "
	            + "child_uuids TEXT DEFAULT '')"; // Store children UUIDs as comma-separated values
	    statement.execute(createAnswersTable);


	    
	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	// Registers a new user in the database.
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, password, role) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole().stream()
					.map(role -> String.valueOf(role.getRoleId()))
				    .collect(Collectors.joining(","))
			);

			pstmt.executeUpdate();
		}
	}

	// Validates a user's login credentials.
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole().stream()
					.map(role -> String.valueOf(role.getRoleId()))
				    .collect(Collectors.joining(","))
			);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	// Checks if a user already exists in the database based on their userName.
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
	
	// Retrieves the role of a user from the database using their UserName.
	public ArrayList<UserRole> getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    ArrayList<UserRole> roles = new ArrayList<UserRole>();
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	for (String role : rs.getString("role").split(",")) {
	        		roles.add(UserRole.fromInt(Integer.parseInt(role)));
	        	}
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return roles;
	}
	
	// Generates a new invitation code and inserts it into the database.
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
	
	// Validates an invitation code to check if it is unused.
	public boolean validateInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
    // Insert a question into the database
    public void insertQuestion(Question question) throws SQLException {
    	System.out.println("Inserting question:" + question.getTitle());
    	String insertQuery = "INSERT INTO questions (uuid, title, body_text, author, posted_date, edited_date, likes, resolved, resolving_child_uuid) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, question.getUUID().toString());
            pstmt.setString(2, question.getTitle());
            pstmt.setString(3, question.getBodyText());
            pstmt.setString(4, question.getAuthor());
            pstmt.setTimestamp(5, new Timestamp(question.getPostedDate().getTime()));
            pstmt.setTimestamp(6, new Timestamp(question.getEditedDate().getTime()));
            pstmt.setInt(7, question.getLikes());
            pstmt.setBoolean(8, question.getResolved());
            pstmt.setString(9, question.getResolvingChild() != null ? question.getResolvingChild().toString() : "");
            pstmt.executeUpdate();
        }
    }
  


    // Insert an answer into the database
    public void insertAnswer(Answer answer) throws SQLException {
        String insertQuery = "INSERT INTO answers (uuid, body_text, author, posted_date, edited_date, likes, child_uuids) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, answer.getUUID().toString());
            pstmt.setString(2, answer.getBodyText());
            pstmt.setString(3, answer.getAuthor());
            pstmt.setTimestamp(4, new Timestamp(answer.getPostedDate().getTime()));
            pstmt.setTimestamp(5, new Timestamp(answer.getEditedDate().getTime()));
            pstmt.setInt(6, answer.getLikes());
            pstmt.setString(7, ""); // New answer starts with no children
            pstmt.executeUpdate();
        }

    }
    
    public ArrayList<String> convertUUIDToString(ArrayList<UUID> UUIDs) {
    	ArrayList<String> stringUUIDs = new ArrayList<String>();
    	for (UUID uuid : UUIDs) {
    		stringUUIDs.add(uuid.toString());
    	}
    	return stringUUIDs;
    }
    
    // Update an existing Question in the database
    public void updateQuestion(Question question) throws SQLException {
        String updateQuery = "UPDATE questions SET title = ?, body_text = ?, author = ?, "
                           + "posted_date = ?, edited_date = ?, likes = ?, resolved = ?, child_uuids = ?, resolving_child_uuid = ? "
                           + "WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setString(1, question.getTitle());
            pstmt.setString(2, question.getBodyText());
            pstmt.setString(3, question.getAuthor());
            pstmt.setTimestamp(4, new Timestamp(question.getPostedDate().getTime()));
            pstmt.setTimestamp(5, new Timestamp(question.getEditedDate().getTime()));
            pstmt.setInt(6, question.getLikes());
            pstmt.setBoolean(7, question.getResolved());
            pstmt.setString(8, String.join(",", convertUUIDToString(question.getChildren()))); // Convert list to CSV format
            pstmt.setString(9, (question.getResolvingChild() != null ? question.getResolvingChild().toString() : ""));
            pstmt.setString(10, question.getUUID().toString());

            pstmt.executeUpdate();
        }
    }

    // Update an existing Answer in the database
    public void updateAnswer(Answer answer) throws SQLException {
        String updateQuery = "UPDATE answers SET body_text = ?, author = ?, posted_date = ?, "
                           + "edited_date = ?, likes = ?, child_uuids = ? "
                           + "WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setString(1, answer.getBodyText());
            pstmt.setString(2, answer.getAuthor());
            pstmt.setTimestamp(3, new Timestamp(answer.getPostedDate().getTime()));
            pstmt.setTimestamp(4, new Timestamp(answer.getEditedDate().getTime()));
            pstmt.setInt(5, answer.getLikes());
            pstmt.setString(6, String.join(",", convertUUIDToString(answer.getChildren()))); // Convert list to CSV format
            pstmt.setString(7, answer.getUUID().toString());

            pstmt.executeUpdate();
        }
    }
    
    public UUID handleEmpty(String resolvingChild) {
    	if (resolvingChild.isBlank()) {
    		return null;
    	} else {
    		return UUID.fromString(resolvingChild);
    	}
    }

    
    // Retrieve all questions from the database
    public HashMap<UUID, Question> getQuestions() throws SQLException {
        HashMap<UUID, Question> questions = new HashMap<>();
        String query = "SELECT * FROM questions";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Question question = new Question(
                    UUID.fromString(
                    rs.getString("uuid")),
                    rs.getString("child_uuids"), // Comma-separated child UUIDs
                    rs.getString("title"),
                    rs.getString("body_text"),
                    rs.getString("author"),
                    rs.getTimestamp("posted_date"),
                    rs.getTimestamp("edited_date"),
                    rs.getInt("likes"),
                    rs.getBoolean("resolved"),
                    handleEmpty(rs.getString("resolving_child_uuid"))
                );
                questions.put(question.getUUID(), question);
                System.out.println("Question: " + question.getTitle());
            }
            System.out.println();
        }
        return questions;
    }

    
    // Retrieve all answers from the database
    public HashMap<UUID, Answer> getAnswers() throws SQLException {
        HashMap<UUID, Answer> answers = new HashMap<>();
        String query = "SELECT * FROM answers";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Answer answer = new Answer(
                    UUID.fromString(rs.getString("uuid")),
                    rs.getString("child_uuids"), // Comma-separated child UUIDs
                    rs.getString("body_text"),
                    rs.getString("author"),
                    rs.getTimestamp("posted_date"),
                    rs.getTimestamp("edited_date"),
                    rs.getInt("likes")
                );
                answers.put(answer.getUUID(), answer);
            }
        }
        return answers;
    }


	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}

}
