package Application;

import java.sql.SQLException;

public class RoleRequest {
	private String author;
	private String requestText;
	private UserRole requestedRole;
	
	public RoleRequest(String author, String requestText, UserRole requestedRole) {
		this.author = author;
		this.requestText = requestText;
		this.requestedRole = requestedRole;
	}
	
	public void approveRequest() throws SQLException {
		StartCSE360.databaseHelper.getUser(author).getRole().add(requestedRole);
		StartCSE360.databaseHelper.removeRoleRequest(author);
	}
	
	public void rejectRequest() throws SQLException {
		StartCSE360.databaseHelper.removeRoleRequest(author);
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getText() {
		return requestText;
	}
	
	public void editText(String newText) {
		this.requestText = newText;
	}
	
	public UserRole getRequestedRole() {
		return requestedRole;
	}
}
