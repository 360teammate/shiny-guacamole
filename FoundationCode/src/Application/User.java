package Application;

import java.util.ArrayList;
import java.util.HashSet;
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

    private int[] reviewerRatings;	// is used if they have a reviewer role, out of 5 stars
    
    // Constructor to initialize a new User object with userName, password, and role.
    public User(String userName, String password, ArrayList<UserRole> role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        
        this.reviewerRatings = new int[5];
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
    
    public void setRatings(int[] r) {
    	this.reviewerRatings = r;
    }
    
    public int[] getRatings() {
    	return this.reviewerRatings;
    }

    public boolean addRating(String rater, int value) {
    	this.reviewerRatings[value - 1]++;
    	return true;
    }
    
    public double getRatingAverage() {
    	double sum = 0.0;
    	int totalReviews = 0;
    	for (int i = 0; i < this.reviewerRatings.length; i++) {
    		sum += reviewerRatings[i] * (i + 1);
    		totalReviews += reviewerRatings[i];
    	}
    	return (totalReviews == 0) ? 0.0 : (sum / totalReviews);
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