package Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    public User(String userName, String password, ArrayList<UserRole> role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.conversations = new HashMap<>();

        try {
            // Fetch all conversations from the DB
            HashMap<UUID, Conversation> allConvos = StartCSE360.databaseHelper.getConversations();

            for (Conversation convo : allConvos.values()) {
                ArrayList<String> participants = convo.getUsers();
                if (participants.contains(userName)) {
                    // Create a unique key (sorted usernames)
                    List<String> sorted = new ArrayList<>(participants);
                    Collections.sort(sorted);
                    String key = String.join(", ", sorted);

                    this.conversations.put(key, convo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        
    // Sets the role of the user.
    public void setRole(ArrayList<UserRole> newRoles) {	

    	this.role=newRoles;
    }
    
    public void newConversation(String name, Conversation conversation) {
    	this.conversations.put(name, conversation);
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public ArrayList<UserRole> getRole() { return role; }
    public HashMap<String, Conversation> getConversations() { return conversations; }
}