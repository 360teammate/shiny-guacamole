package Application;

import java.util.Date;
import java.util.UUID;

public class Message {
	private UUID uuid;
	private String content;
	private String author;
	private UUID conversation;
	private Date timestamp;
	
	public Message(String content, String author, UUID conversation) {
		this.uuid = UUID.randomUUID();
		this.content = content;
		this.author = author;
		this.conversation = conversation;
		this.timestamp = new Date();
	}
	
	public Message(UUID uuid, String content, String author, UUID conversation, Date timestamp) {
		this.uuid = uuid;
		this.content = content;
		this.author = author;
		this.conversation = conversation;
		this.timestamp = timestamp;
	}
	
	public UUID getUUID() { return uuid; }
	public String getContent() { return content; }
	public String getAuthor() { return author; }
	public UUID getConversation() { return conversation; }
	public Date getTimestamp() { return timestamp; }

}
