package Application;

import java.sql.SQLException;

public class RoleRequest {
	private String author;
	private String requestText;
	private UserRole requestedRole;
	
	/**
	 * Constructor for a roleRequest object
	 * @param author
	 * @param requestText
	 * @param requestedRole
	 */
	public RoleRequest(String author, String requestText, UserRole requestedRole) {
		this.author = author;
		this.requestText = requestText;
		this.requestedRole = requestedRole;
	}
	
	/*
	 * Approves a request, sets user to requested role, removes request from database
	 */
	public void approveRequest() throws SQLException {
		StartCSE360.databaseHelper.getUser(author).getRole().add(requestedRole);
		StartCSE360.databaseHelper.removeRoleRequest(author);
	}
	
	/**
	 * Rejects a request, removes request from database
	 * @throws SQLException
	 */
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
