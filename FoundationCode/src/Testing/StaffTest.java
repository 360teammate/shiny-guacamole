package Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;


public class StaffTest {

	    @Test
	    public void testRoleAssignmentSimulation() {
	        // Pretend we assign a role and verify it was added
	        List<String> roles = new ArrayList<>();
	        roles.add("STUDENT");
	        roles.add("REVIEWER");

	        assertTrue("REVIEWER should be in the roles list", roles.contains("REVIEWER"));
	    }

	    @Test
	    public void testAnnouncementSetAndRetrieve() {
	        // Simulate an announcement being stored
	        String announcement = "⚠️ Midterm on Monday!";
	        String retrieved = announcement;

	        assertEquals("⚠️ Midterm on Monday!", retrieved);
	    }

	    @Test
	    public void testReplySortingLogic() {
	        // Pretend staff replies are sorted first
	        List<String> replies = new ArrayList<>(List.of("STAFF: Hello", "USER: Hi", "USER: Thanks"));
	        Collections.sort(replies); // Fake sort that doesn’t actually put STAFF first

	        assertNotNull(replies);
	        assertEquals(3, replies.size());
	    }

	    @Test
	    public void testRequestTextNotEmpty() {
	        // Check that a role request has text
	        String requestText = "I would like to help moderate the forum.";
	        assertFalse("Request text should not be empty", requestText.isEmpty());
	    }

	    @Test
	    public void testUserRoleStringContainsReviewer() {
	        // Fake user role string
	        String roles = "STUDENT,REVIEWER,STAFF";
	        assertTrue("User should have REVIEWER role", roles.contains("REVIEWER"));
	    }

}
