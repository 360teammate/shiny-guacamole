package Application;

import java.util.HashMap;
import java.util.UUID;


public class ReviewList {
	private HashMap<UUID, Reviewer> reviews;
	
	public ReviewList() {
		this.reviews = new HashMap<>();
	}
	
	/**
     * Adds a new review to the list.
     *
     * @param reviewerId ID of the reviewer.
     * @param questionId ID of the reviewed question.
     * @param answerId (Optional) ID of the reviewed answer.
     * @param content Content of the review.
     * @return The UUID of the newly created review.
     */
	public UUID addReview(int reviewerId, int questionId, UUID answerId, String content) {
		UUID reviewId = UUID.randomUUID();
		Reviewer newReview = new Reviewer(reviewId, reviewerId, questionId, answerId, content);
		reviews.put(reviewId, newReview);
		return reviewId;
	}
	
	/**
     * Retrieves a review by its UUID.
     *
     * @param reviewId The UUID of the review.
     * @return The corresponding review object or null if not found.
     */
    public Reviewer getReview(UUID reviewId) {
        return reviews.get(reviewId);
    }
    
    /**
     * Updates the content of an existing review.
     *
     * @param reviewId The UUID of the review to update.
     * @param newContent The new content for the review.
     */
    public void updateReview(UUID reviewId, String newContent) {
    	if (reviews.containsKey(reviewId)) {
    		reviews.get(reviewId).setContent(newContent);
    	}
    }
    
    /**
     * Deletes a review by its UUID.
     *
     * @param reviewId The UUID of the review to remove.
     */
    public void deleteReview(UUID reviewId) {
    	reviews.remove(reviewId);
    }
    
    /**
     * Retrieves all reviews.
     *
     * @return A HashMap containing all reviews.
     */
    public HashMap<UUID, Reviewer> getAllReviews() {
    	return reviews;
    }
}
