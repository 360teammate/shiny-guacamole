package Application;

public class RoleRequest {
	private String author;
	private String requestText;
	private UserRole requestedRole;
	
	public RoleRequest(String author, String requestText, UserRole requestedRole) {
		this.author = author;
		this.requestText = requestText;
		this.requestedRole = requestedRole;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getText() {
		return requestText;
	}
	
	public UserRole getRequestedRole() {
		return requestedRole;
	}
}
