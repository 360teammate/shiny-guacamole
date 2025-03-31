package Application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String password;
    private ArrayList<UserRole> role;
    private HashMap<String, Conversation> conversations;
    
    // Constructor to initialize a new User object from the database
    public User(String userName) {
    	this.userName = userName;
    	// TODO get password, roles, and conversations from the database
    }

    // Constructor to initialize a new User object with userName, password, and role.
    public User(String userName, String password, UserRole role) {
        this.userName = userName;
        this.password = password;
        this.role = new ArrayList<UserRole>();
        this.role.add(role);
        this.conversations = new HashMap<String, Conversation>();
    }
    
    // Sets the role of the user.
    public void setRole(ArrayList<UserRole> role) {
    	this.role=role;
    }
    
    public void newConversation(String name, Conversation conversation) {
    	this.conversations.put(name, conversation);
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public ArrayList<UserRole> getRole() { return role; }
    public HashMap<String, Conversation> getConversations() { return conversations; }
}