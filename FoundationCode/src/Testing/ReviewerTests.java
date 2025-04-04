package Testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import Application.Reviewer;
import java.util.UUID;

public class ReviewerTests {
    private Reviewer reviewer;
    private UUID reviewId;
    private int reviewerId;
    private int questionId;
    private UUID answerId;
    private String content;

    @Before
    public void setUp() {
        // Setup test data
        reviewId = UUID.randomUUID();
        reviewerId = 1;
        questionId = 101;
        answerId = UUID.randomUUID();
        content = "This is a test review.";

        // Create the Reviewer object
        reviewer = new Reviewer(reviewId, reviewerId, questionId, answerId, content);
    }

    @Test
    public void testReviewerConstructor() {
        // Test if the constructor correctly initializes the reviewer object
        assertNotNull("Reviewer should be created", reviewer);
        assertEquals("Review ID should match", reviewId, reviewer.getReviewId());
        assertEquals("Reviewer ID should match", reviewerId, reviewer.getReviewerId());
        assertEquals("Question ID should match", questionId, reviewer.getQuestionId());
        assertEquals("Answer ID should match", answerId, reviewer.getAnswerId());
        assertEquals("Content should match", content, reviewer.getContent());
    }

    // Test updating the review content
    @Test
    public void testSetContent() {
        String newContent = "Updated review content";
        reviewer.setContent(newContent);
        assertEquals("Content should be updated", newContent, reviewer.getContent());
    }
    
    // Test setting content to null
    @Test
    public void testNullContentHandling() {
        reviewer.setContent(null);
        assertNull("Content should be null", reviewer.getContent());
    }
    
    // Test setting content to an empty string
    @Test
    public void testEmptyContent() {
        reviewer.setContent("");
        assertEquals("Content should be empty string", "", reviewer.getContent());
    }
    
    // Test that answer ID is stored and returned correctly
    @Test
    public void testAnswerIdConsistency() {
        UUID newAnswerId = UUID.randomUUID();
        Reviewer customReviewer = new Reviewer(UUID.randomUUID(), 2, 202, newAnswerId, "Another review");
        assertEquals("Answer ID should match", newAnswerId, customReviewer.getAnswerId());
    }
}