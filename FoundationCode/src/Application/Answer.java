package Application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Represents an answer in a Q&A system, with attributes for its text,
 * author, timestamps, likes, and child answers.
 */
public class Answer {
    private UUID uuid; // Unique identifier for the answer
    private ArrayList<UUID> childrenIDs; // List of child answer UUIDs (for nested replies)

    private String bodyText; // The text content of the answer
    private String author; // The name of the answer's author
    private Date postedDate; // The date when the answer was first posted
    private Date editedDate; // The date when the answer was last edited

    private int likes; // Number of likes the answer has received

    /**
     * Constructor for creating a new answer with a unique UUID.
     *
     * @param uuid      Unique identifier for the answer.
     * @param bodyText  The content of the answer.
     * @param author    The author of the answer.
     */
    public Answer(UUID uuid, String bodyText, String author) {
        this.uuid = uuid;
        this.childrenIDs = new ArrayList<>(); // Initialize an empty list of child answers

        this.bodyText = bodyText;
        this.author = author;
        this.postedDate = new Date(); // Set current time as posted date
        this.editedDate = new Date(); // Set current time as edited date

        this.likes = 0; // Initialize likes to zero
    }

    /**
     * Constructor for loading an existing answer from stored data.
     *
     * @param uuid        Unique identifier for the answer.
     * @param children    Comma-separated string of child answer UUIDs.
     * @param bodyText    The content of the answer.
     * @param author      The author of the answer.
     * @param postedDate  The original posting date.
     * @param editedDate  The last edited date.
     * @param likes       The number of likes the answer has received.
     */
    public Answer(UUID uuid, String children, String bodyText, String author, Date postedDate, Date editedDate, int likes) {
        this.uuid = uuid;
        this.childrenIDs = new ArrayList<>();

        // Convert comma-separated child UUIDs into an ArrayList
        for (String uuidStr : children.split(",")) {
            if (!uuidStr.isBlank()) {
                childrenIDs.add(UUID.fromString(uuidStr));
            }
        }

        this.bodyText = bodyText;
        this.author = author;
        this.postedDate = postedDate;
        this.editedDate = editedDate;
        this.likes = likes;
    }

    /**
     * Edits the answer's text and updates the edited date.
     *
     * @param newBodyText The new content of the answer.
     */
    public void editBodyText(String newBodyText) {
        this.bodyText = newBodyText;
        this.editedDate = new Date(); // Update edited timestamp
    }

    /**
     * Adds a child answer UUID to the list of children.
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
    
    public String getEditedDateAsString() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(this.getEditedDate());
    }
	
	public UUID getUUID() { return this.uuid; }
	public ArrayList<UUID> getChildren() { return this.childrenIDs; }
	
	public String getBodyText() { return this.bodyText; }
	public String getAuthor() { return this.author; }
	public Date getPostedDate() { return this.postedDate; }
	public Date getEditedDate() { return this.editedDate; }
	
	public int getLikes() { return this.likes; }

	
}
