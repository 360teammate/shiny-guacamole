package offlineTestingHelpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import Application.Conversation;
import Application.StartCSE360;
import Application.UserRole;

public class OfflineUser {
	private String userName;
    private String password;
    private ArrayList<UserRole> role;
    private HashMap<String, Conversation> conversations;
    
    // Constructor to initialize a new User object from the database
    public OfflineUser(String userName) {
    	this.userName = userName;
    	// TODO get password, roles, and conversations from the database
    }

    private ArrayList<String> raters;
    private int[] reviewerRatings;	// is used if they have a reviewer role, out of 5 stars
    
    // Constructor to initialize a new User object with userName, password, and role.
    public OfflineUser(String userName, String password, ArrayList<UserRole> role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        
        this.raters = new ArrayList<>();
        this.raters.add(userName);
        this.reviewerRatings = new int[5];
        
        this.conversations = new HashMap<>();

        /*
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
        */
    }
    
    public ArrayList<String> getRaters() {
    	return this.raters;
    }
    
    public void addRater(String name) {
    	this.raters.add(name);
    	// StartCSE360.databaseHelper.updateRaters(userName, name);
    }
    
    public void setRaters(ArrayList<String> r) {
    	this.raters = r;
    	// StartCSE360.databaseHelper.updateRaters(userName, password);
    }
    
    public void setRatings(int[] r) {
    	this.reviewerRatings = r;
    }
    
    public int[] getRatings() {
    	return this.reviewerRatings;
    }

    public boolean addRating(int value) {
    	this.reviewerRatings[value - 1]++;
    	// StartCSE360.databaseHelper.updateUserRatings(userName, value - 1);
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
