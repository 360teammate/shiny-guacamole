package Application;

import java.util.ArrayList;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String password;
    private ArrayList<UserRole> role;

    private int[] reviewerRatings;	// is used if they have a reviewer role, out of 5 stars
    
    // Constructor to initialize a new User object with userName, password, and role.
    public User(String userName, String password, UserRole role) {
        this.userName = userName;
        this.password = password;
        this.role = new ArrayList<UserRole>();
        this.role.add(role);
        
        this.reviewerRatings = new int[5];
    }
    
    public int[] getRatings() {
    	return this.reviewerRatings;
    }
    
    public void addRating(int value) {
    	this.reviewerRatings[value - 1]++;
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

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public ArrayList<UserRole> getRole() { return role; }
}