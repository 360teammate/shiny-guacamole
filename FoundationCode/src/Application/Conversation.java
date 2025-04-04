package Application;

import java.util.ArrayList;
import java.util.UUID;

public class Conversation {
	private UUID uuid;
	private ArrayList<String> users;
	private ArrayList<Message> messages;
	
	public Conversation(ArrayList<String> users) {
		this.uuid = UUID.randomUUID();
		this.users = users;
		this.messages = new ArrayList<Message>();
	}
	
	public Conversation(UUID uuid, ArrayList<String> users, ArrayList<Message> messages) {
		this.uuid = uuid;
		this.users = users;
		this.messages = messages;
	}
	
	public UUID getUUID() { return this.uuid; }
	public ArrayList<String> getUsers() { return this.users; }
	public ArrayList<Message> getMessages() { return this.messages; }
	

}
