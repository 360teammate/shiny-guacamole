package Application;

import java.sql.Timestamp;

public class Announcement {
    private int id;
    private String author;
    private String content;
    private Timestamp timestamp;

    public Announcement(int id, String author, String content, Timestamp timestamp) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Add getter for id
    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
