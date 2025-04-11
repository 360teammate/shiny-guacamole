package Database;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import Application.Question;
import Application.Answer;
import Application.AnswerList;
import Application.Answer;
import Application.Conversation;
import Application.Message;
import Application.Question;
import Application.RoleRequest;
import Application.User;
import Application.UserRole;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:./src/Database/databasePart1";

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
//			statement.execute("DROP ALL OBJECTS");

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
				+ "role VARCHAR(225), "
				+ "conversations TEXT DEFAULT '', "  // Comma-separated UUIDs
				+ "raters TEXT NOT NULL, "		// list of users that have rated this user
				+ "ratings TEXT NOT NULL)";		// array of 5 values of number of ratings in that category for this reviewer user
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
	    
	    String createConversationsTable = "CREATE TABLE IF NOT EXISTS conversations ("
	            + "uuid VARCHAR(36) PRIMARY KEY, "
	            + "users TEXT NOT NULL)";
	    statement.execute(createConversationsTable);
	    String createRoleRequestTable = "CREATE TABLE IF NOT EXISTS RoleRequest ("
	            + "id INT AUTO_INCREMENT PRIMARY KEY, "
	            + "author VARCHAR(255) NOT NULL, "
	            + "requestText TEXT NOT NULL, "
	            + "requestedRole INT NOT NULL)";
	    statement.execute(createRoleRequestTable);

	    String createMessagesTable = "CREATE TABLE IF NOT EXISTS messages ("
	            + "uuid VARCHAR(36) PRIMARY KEY, "
	            + "content TEXT NOT NULL, "
	            + "author VARCHAR(255) NOT NULL, "
	            + "conversation VARCHAR(36) NOT NULL, "
	            + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
	            + "FOREIGN KEY (conversation) REFERENCES conversations(uuid))";
	    statement.execute(createMessagesTable);
	    
	    String createAnnouncementsTable = "CREATE TABLE IF NOT EXISTS announcements ("
	            + "id INT AUTO_INCREMENT PRIMARY KEY, "
	            + "content TEXT NOT NULL, "
	            + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
	    statement.execute(createAnnouncementsTable);

	    
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
		String insertUser = "INSERT INTO cse360users (userName, password, role, raters, ratings) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole().stream()
					.map(role -> String.valueOf(role.getRoleId())).collect(Collectors.joining(","))
			);
			pstmt.setString(4, user.getUserName());
			String ratingsStr = Arrays.stream(user.getRatings())
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(","));
			pstmt.setString(5, ratingsStr);

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
					.map(role -> String.valueOf(role.getRoleId())).collect(Collectors.joining(","))
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
//	        		System.out.println(role);
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
    
    // Deletes an answer from the database based on its UUID
    public void deleteAnswer(UUID answerUUID) throws SQLException {
        String deleteQuery = "DELETE FROM answers WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
            pstmt.setString(1, answerUUID.toString());
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
    
    public void insertRoleRequest(String author, String requestText, UserRole requestedRole) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM RoleRequest WHERE author = ?";
        String insertQuery = "INSERT INTO RoleRequest (author, requestText, requestedRole) VALUES (?, ?, ?)";
        
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, author);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    // Author already exists, do not insert
                	throw new SQLException("A role request for this author already exists.");
                }
            }
        }

        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, author);
            insertStatement.setString(2, requestText);
            insertStatement.setInt(3, requestedRole.getRoleId());
            insertStatement.executeUpdate();
        }
    }
    
    public void removeRoleRequest(String author) throws SQLException {
        // Query to check if the author exists in the database
        String checkQuery = "SELECT COUNT(*) FROM RoleRequest WHERE author = ?";
        String deleteQuery = "DELETE FROM RoleRequest WHERE author = ?";
        
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, author);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    // No role request found for this author
                    throw new SQLException("No role request found for this author.");
                }
            }
        }

        // Proceed to delete the role request for the given author
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setString(1, author);
            deleteStatement.executeUpdate();
        }
    }

    
    public ArrayList<RoleRequest> getRoleRequests() throws SQLException {
        String query = "SELECT id, author, requestText, requestedRole FROM RoleRequest";
        ArrayList<RoleRequest> requests = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String author = resultSet.getString("author");
                String requestText = resultSet.getString("requestText");
                int roleId = resultSet.getInt("requestedRole");

                UserRole role = UserRole.fromInt(roleId); // Convert int back to UserRole enum
                requests.add(new RoleRequest(author, requestText, role));
            }
        }
        return requests;
    }
    
    public ArrayList<User> getPendingRequests() throws SQLException {
        ArrayList<User> pendingUsers = new ArrayList<>();
        ArrayList<String> requestAuthors = new ArrayList<>();
        
        for (RoleRequest request : getRoleRequests()) {
        	requestAuthors.add(request.getAuthor());
        }
        String query = "SELECT userName, password, role FROM cse360users WHERE userName = ?";

        for (String author : requestAuthors) {
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setString(1, author );
	            ResultSet rs = pstmt.executeQuery();
	
	            while (rs.next()) {
	                String userName = rs.getString("userName");
	                String password = rs.getString("password");
	                int roleValue = rs.getInt("role");
	                UserRole role = UserRole.fromInt(roleValue);
	                ArrayList<UserRole> roles = new ArrayList<>();
	                roles.add(role);
	                
	                pendingUsers.add(new User(userName, password, roles));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
        }

        return pendingUsers;
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
	
	// Returns all usernames in arraylist
	public ArrayList<String> getAllUsernames() {
	    ArrayList<String> usernames = new ArrayList<>();
	    String query = "SELECT userName FROM cse360users";

	    try (PreparedStatement pstmt = connection.prepareStatement(query);
	         ResultSet rs = pstmt.executeQuery()) {
	        
	        while (rs.next()) {
	            usernames.add(rs.getString("userName"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return usernames;
	}
	
	public User getUser(String userName) {
        String query = "SELECT * FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String password = rs.getString("password");
                String roleString = rs.getString("role");
                ArrayList<UserRole> roles = new ArrayList<>();

                for (String roleId : roleString.split(",")) {
                    roles.add(UserRole.fromInt(Integer.parseInt(roleId)));
                }

                return new User(userName, password, roles);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User not found
    }
	
	public void insertMessage(Message m) throws SQLException {
	    String insertQuery = "INSERT INTO messages (uuid, content, author, conversation, timestamp) "
	                       + "VALUES (?, ?, ?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
	        pstmt.setString(1, m.getUUID().toString());
	        pstmt.setString(2, m.getContent());
	        pstmt.setString(3, m.getAuthor());
	        pstmt.setString(4, m.getConversation().toString());
	        pstmt.setTimestamp(5, new Timestamp(m.getTimestamp().getTime()));
	        pstmt.executeUpdate();
	    }
	}
	
	
	public void updateUserRoles(String userName, ArrayList<UserRole> newRoles) throws SQLException {
	    String updateQuery = "UPDATE cse360users SET role = ? WHERE userName = ?";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
	        String rolesString = newRoles.stream()
	                .map(role -> String.valueOf(role.getRoleId())) // Convert each role to its ID
	                .collect(Collectors.joining(",")); // Join IDs with commas
	        
	        pstmt.setString(1, rolesString);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    }
	}
	
	
	public void insertConversation(Conversation c) throws SQLException {
	    String insertQuery = "INSERT INTO conversations (uuid, users) VALUES (?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
	        pstmt.setString(1, c.getUUID().toString());
	        pstmt.setString(2, String.join(",", c.getUsers()));
	        pstmt.executeUpdate();
	    }

	    // Optionally insert all messages with this
	    for (Message m : c.getMessages()) {
	        insertMessage(m);
	    }
	}
	
	public HashMap<UUID, Conversation> getConversations() throws SQLException {
	    HashMap<UUID, Conversation> conversations = new HashMap<>();
	    String query = "SELECT * FROM conversations";

	    try (PreparedStatement pstmt = connection.prepareStatement(query);
	         ResultSet rs = pstmt.executeQuery()) {
	        while (rs.next()) {
	            UUID uuid = UUID.fromString(rs.getString("uuid"));
	            ArrayList<String> users = new ArrayList<>(List.of(rs.getString("users").split(",")));
	            ArrayList<Message> messages = getMessagesByConversation(uuid);

	            conversations.put(uuid, new Conversation(uuid, users, messages));
	        }
	    }

	    return conversations;
	}

	public ArrayList<Message> getMessagesByConversation(UUID conversationId) throws SQLException {
	    ArrayList<Message> messages = new ArrayList<>();
	    String query = "SELECT * FROM messages WHERE conversation = ? ORDER BY timestamp ASC";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, conversationId.toString());
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Message m = new Message(
	                UUID.fromString(rs.getString("uuid")),
	                rs.getString("content"),
	                rs.getString("author"),
	                UUID.fromString(rs.getString("conversation")),
	                rs.getTimestamp("timestamp")
	            );
	            messages.add(m);
	        }
	    }

	    return messages;
	}

	public void updateUserConversations(String userName, ArrayList<UUID> conversations) throws SQLException {
	    String updateQuery = "UPDATE cse360users SET conversations = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
	        String csv = conversations.stream()
	                .map(UUID::toString)
	                .collect(Collectors.joining(","));
	        pstmt.setString(1, csv);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    }
	}

	public ArrayList<UUID> getUserConversationUUIDs(String userName) {
	    ArrayList<UUID> conversationUUIDs = new ArrayList<>();
	    String query = "SELECT conversations FROM cse360users WHERE userName = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String raw = rs.getString("conversations");
	            if (raw != null && !raw.isBlank()) {
	                for (String id : raw.split(",")) {
	                    if (!id.isBlank()) {
	                        conversationUUIDs.add(UUID.fromString(id.trim()));
	                    }
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return conversationUUIDs;
	}

	public ArrayList<String> getRaters(String username) {
		ArrayList<String> ans = new ArrayList<>();
		String selectSQL = "SELECT raters FROM cse360users WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
            	String ratersStr = rs.getString("raters");
                if (ratersStr != null && !ratersStr.isEmpty()) {
                    ans = new ArrayList<>(Arrays.asList(ratersStr.split(",")));
                }
            }
        }
		catch (SQLException e) {
            e.printStackTrace();
        }
		return ans;
	}
	
	public void updateRaters(String username, String rater) {
		ArrayList<String> ans = getRaters(username);
		ans.add(rater);
		String updateSQL = "UPDATE cse360users SET raters = ? WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            String ratersStr = String.join(",", ans);
            pstmt.setString(1, ratersStr);
            pstmt.setString(2, username);

            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public int[] getRatings(String username) {
        String selectSQL = "SELECT ratings FROM cse360users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Convert the comma-separated string into an int array
                String ratingsStr = rs.getString("ratings");
                return Arrays.stream(ratingsStr.split(","))
                             .mapToInt(Integer::parseInt)
                             .toArray();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[0]; // Return an empty array if user not found or error occurs
    }
	
	public void updateUserRatings(String username, int rating) {
		String selectSQL = "SELECT ratings FROM cse360users WHERE username = ?";
        String updateSQL = "UPDATE cse360users SET ratings = ? WHERE username = ?";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL)) {
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String ratingsStr = rs.getString("ratings");
                String[] ratingArray = ratingsStr.split(",");

                if (rating >= 0 && rating < ratingArray.length) {
                    int[] ratings = Arrays.stream(ratingArray)
                                          .mapToInt(Integer::parseInt)
                                          .toArray();
                    ratings[rating]++;
                    String updatedRatingsStr = Arrays.stream(ratings)
                                                     .mapToObj(String::valueOf)
                                                     .collect(Collectors.joining(","));

                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
                        updateStmt.setString(1, updatedRatingsStr);
                        updateStmt.setString(2, username);
                        updateStmt.executeUpdate();
                        System.out.println("Updated ratings for " + username + ": " + updatedRatingsStr);
                    }
                }
                else {
                    System.out.println("Index out of range!");
                }
            } 
            else {
                System.out.println("User not found.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public void setLatestAnnouncement(String content) {
	    String insert = "INSERT INTO announcements (content) VALUES (?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(insert)) {
	        pstmt.setString(1, content);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public String getLatestAnnouncement() {
	    String query = "SELECT content FROM announcements ORDER BY created_at DESC LIMIT 1";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("content");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}
