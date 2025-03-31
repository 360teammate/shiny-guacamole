
/*
 * An enumeration that contains all possible roles that can be assigned to a user in the system.
 * Each role is associated with an integer id for simple comparisons and a String for formatted printing.
 * A user can have multiple roles assigned at the same time.
 */

package Application;

public enum UserRole {
	
	//----------------------------------------------//
	
	NEW_USER 	(-1, "New User"),
	ADMIN		(0, "Admin"),
	STUDENT		(1, "Student"),
	REVIEWER	(2, "Reviewer"),
	INSTRUCTOR	(3, "Instructor"),
	STAFF		(4, "Staff");
	
	//----------------------------------------------//
	
	private final int roleId;
	private final String roleText;

	// enum constructor must be private
	private UserRole(int roleId, String roleText) {
		this.roleId = roleId;
		this.roleText = roleText;
	}

	// get a numerical identification of a user role
	public int getRoleId() {
		return this.roleId;
	}
	
	// get a formatted String of a user role
	@Override
	public String toString() {
		return this.roleText;
	}
	
	// Convert an integer to a UserRole
		public static UserRole fromInt(int id) {
			for (UserRole role : UserRole.values()) {
				if (role.roleId == id) {
					return role;
				}
			}
			throw new IllegalArgumentException("Invalid UserRole ID: " + id);
		}
	
} // end UserRole
