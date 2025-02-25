package application;

import java.util.ArrayList;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String password;
    private ArrayList<UserRole> role;

    // Constructor to initialize a new User object with userName, password, and role.
    public User( String userName, String password, UserRole role) {
        this.userName = userName;
        this.password = password;
        this.role = new ArrayList<UserRole>();
        this.role.add(role);
    }
    
    // Sets the role of the user.
    public void setRole(ArrayList<UserRole> role) {
    	this.role=role;
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public ArrayList<UserRole> getRole() { return role; }
}

// admin
//	abram 12345
// invite
//	b163