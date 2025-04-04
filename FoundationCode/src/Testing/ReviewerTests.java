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

    
    @Test
    public void testSetContent() {
        String newContent = "Updated review content";
        reviewer.setContent(newContent);
        assertEquals("Content should be updated", newContent, reviewer.getContent());
    }
    
    @Test
    public void testGetReviewId() {
    	int reviewIdResult = reviewer.getReviewerId();
    	assertEquals("1", 1, reviewIdResult);
    }
    
    @Test
    public void testGetQuestionid() {
    	int questionIdResult = reviewer.getQuestionId();
    	assertEquals("101", 101, questionIdResult);
    }
    
    @Test
    public void testGetAnswerId() {
    	UUID expectedAnswerId = UUID.randomUUID();
    	reviewer = new Reviewer(UUID.randomUUID(), 1, 101, expectedAnswerId, "Sample review content");
    	UUID answerIdResult = reviewer.getAnswerId();
    	assertEquals("Answer ID should match", expectedAnswerId, answerIdResult);
    }
}
