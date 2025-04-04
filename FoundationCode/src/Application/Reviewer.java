package Application;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Represents a review submitted by a reviewer for a specific answer to a question.
 * Each review contains a unique identifier, references to the reviewer, question, 
 * and answer, as well as the review content and timestamp.
 */
public class Reviewer {
	private UUID reviewId; // Unique identifier for the review
    private int reviewerId; // ID of the reviewer who created the review
    private int questionId; // ID of the question associated with the review
    private UUID answerId; // ID of the answer being reviewed
    private String content; // Text content of the review
    private LocalDateTime timestamp; // Time stamp of when the review was last modified
	
	/**
     * Constructs a new Reviewer objects.
     *
     * @param reviewId   Unique identifier for the review.
     * @param reviewerId ID of the reviewer who wrote the review.
     * @param questionId ID of the question associated with the review.
     * @param answerId   ID of the answer being reviewed.
     * @param content    Content of the review.
     */
	public Reviewer(UUID reviewId, int reviewerId, int questionId, UUID answerId, String content) {
		this.reviewId = reviewId;
		this.reviewerId = reviewerId;
		this.questionId = questionId;
		this.answerId = answerId;
		this.content = content;
	}
	
	// Gets reviewId
	public UUID getReviewId() {
		return reviewId;
	}
	
	// Gets reviewerid
	public int getReviewerId() {
		return reviewerId;
	}
	
	// Gets questionId
	public int getQuestionId() {
		return questionId;
	}
	
	// Gets answerId
	public UUID getAnswerId() {
		return answerId;
	}
	
	// Gets the review content as a string
	public String getContent() {
		return content;
	}
	
	// Sets the new content
	public void setContent(String content) {
		this.content = content;
		this.timestamp = LocalDateTime.now();
	}
}
