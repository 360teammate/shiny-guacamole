package Application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a question in a Q&A system, containing metadata such as title, 
 * body text, author, timestamps, likes, resolution status, and child answers.
 */
public class Question {
    private UUID uuid; // Unique identifier for the question
    private ArrayList<UUID> childrenIDs; // List of UUIDs for answers (children)
    private UUID resolvingChildID; // UUID for answer resolving question

    private String title; // The title of the question
    private String bodyText; // The body content of the question
    private String author; // The name of the person who posted the question
    private Date postedDate; // The date when the question was first posted
    private Date editedDate; // The date when the question was last edited

    private int likes; // Number of likes the question has received
    private boolean resolved; // Whether the question has been marked as resolved

    /**
     * Constructor for creating a new question.
     *
     * @param uuid      Unique identifier for the question.
     * @param title     The title of the question.
     * @param bodyText  The content of the question.
     * @param author    The author of the question.
     */
    public Question(UUID uuid, String title, String bodyText, String author) {
        this.uuid = uuid;
        this.childrenIDs = new ArrayList<>(); // Initialize an empty list of answers
        resolvingChildID = null; // Default to no resolving question

        this.title = title;
        this.bodyText = bodyText;
        this.author = author;
        this.postedDate = new Date(); // Set the current date as the posted date
        this.editedDate = new Date(); // Set the current date as the edited date

        this.likes = 0; // Initialize likes to zero
        this.resolved = false; // Default to unresolved
    }

    /**
     * Constructor for loading an existing question from stored data.
     *
     * @param uuid           Unique identifier for the question.
     * @param children       Comma-separated string of child answer UUIDs.
     * @param title          The title of the question.
     * @param bodyText       The content of the question.
     * @param author         The author of the question.
     * @param postedDate     The original posting date.
     * @param editedDate     The last edited date.
     * @param likes          The number of likes the question has received.
     * @param resolved       Whether the question is resolved.
     * @param resolvingChild UUID for child that resolves question
     */
    public Question(UUID uuid, String children, String title, String bodyText, String author, Date postedDate, Date editedDate, int likes, boolean resolved, UUID resolvingChildID) {
        this.uuid = uuid;
        this.childrenIDs = new ArrayList<>();

        // Convert comma-separated child UUIDs into an ArrayList
        for (String uuidStr : children.split(",")) {
            if (!uuidStr.isBlank()) {
                childrenIDs.add(UUID.fromString(uuidStr));
            }
        }

        this.title = title;
        this.bodyText = bodyText;
        this.author = author;
        this.postedDate = postedDate;
        this.editedDate = editedDate;
        this.likes = likes;
        this.resolved = resolved;
        this.resolvingChildID = resolvingChildID;
    }
    
    
    // TODO: REMOVE EXISTS FOR TESTING PURPOSES DUE TO DATABASE UNFINISHED
    public Question(UUID uuid, String children, String title, String bodyText, String author, Date postedDate, Date editedDate, int likes, boolean resolved) {
        this.uuid = uuid;
        this.childrenIDs = new ArrayList<>();

        // Convert comma-separated child UUIDs into an ArrayList
        for (String uuidStr : children.split(",")) {
            if (!uuidStr.isBlank()) {
                childrenIDs.add(UUID.fromString(uuidStr));
            }
        }

        this.title = title;
        this.bodyText = bodyText;
        this.author = author;
        this.postedDate = postedDate;
        this.editedDate = editedDate;
        this.likes = likes;
        this.resolved = resolved;
    }
    
    /**
     * Edits the title of the question and updates the edited date.
     *
     * @param newTitle The new title of the question.
     */
    public void editTitle(String newTitle) {
        this.title = newTitle;
        this.editedDate = new Date(); // Update edited timestamp
    }

    /**
     * Edits the body text of the question and updates the edited date.
     *
     * @param newBodyText The new content of the question.
     */
    public void editBodyText(String newBodyText) {
        this.bodyText = newBodyText;
        this.editedDate = new Date(); // Update edited timestamp
    }
    
    /**
     * Adds a child answer UUID to the list of child answers.
     *
     * @param uuid The UUID of the child answer.
     */
    public void addChild(UUID uuid) {
        this.childrenIDs.add(uuid);
    }

    /**
     * Formats and returns the posted date as a readable string.
     *
     * @return The formatted date string (e.g., "Feb 14, 2025").
     */
    public String getDateAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(this.getPostedDate());
    }
    
    /**
     * 
     * @param UUID The UUID of the resolving child
     */
    public void resolveQuestion(UUID UUID) { 
    	resolved = true; 
    	resolvingChildID = UUID;
    	}

    public String getEditedDateAsString() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(this.getEditedDate());
    }
	
	public UUID getUUID() { return this.uuid; }
	public ArrayList<UUID> getChildren() { return this.childrenIDs; }
	
	public String getTitle() { return this.title; }
	public String getBodyText() { return this.bodyText; }
	public String getAuthor() { return this.author; }
	public Date getPostedDate() { return this.postedDate; }
	public Date getEditedDate() { return this.editedDate; }
	
	public int getLikes() { return this.likes; }
	public boolean getResolved() { return this.resolved; }
	public UUID getResolvingChild() { return this.resolvingChildID; }
	
	public void removeResolvingChild() {
	    this.resolvingChildID = null; // Set resolving child to null
	    this.resolved = false; // Mark the question as unresolved
	}
	
}
