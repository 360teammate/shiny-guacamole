package Application;
import java.time.LocalDateTime;
import java.util.UUID;

public class Reviewer {
	private UUID reviewId;
	private int reviewerId;
	private int questionId;
	private UUID answerId;
	private String content;
	private LocalDateTime timestamp;
	
	public Reviewer(UUID reviewId, int reviewerId, int questionId, UUID answerId, String content) {
		this.reviewId = reviewId;
		this.reviewerId = reviewerId;
		this.questionId = questionId;
		this.answerId = answerId;
		this.content = content;
	}
	
	public UUID getReviewId() {
		return reviewId;
	}
	
	public int getReviewerId() {
		return reviewerId;
	}
	
	public int getQuestionId() {
		return questionId;
	}
	
	public UUID getAnswerId() {
		return answerId;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
		this.timestamp = LocalDateTime.now();
	}
}
