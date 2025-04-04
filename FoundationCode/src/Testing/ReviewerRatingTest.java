package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Application.User;
import Application.UserRole;
import offlineTestingHelpers.OfflineUser;

// Tests regarding reviewer ratings
// With a modified User class that does not access the database
public class ReviewerRatingTest {
	
	@Test
	public void testConstructor() {
		OfflineUser dummy = new OfflineUser("", "", null);
		
		assertArrayEquals(dummy.getRatings(), new int[] { 0, 0, 0, 0, 0 });
		assertEquals(dummy.getRaters().size(), 1);
	}
	
	@Test
	public void testAddReview() {
		OfflineUser dummy = new OfflineUser("", "", null);
		dummy.addRating(5);
		dummy.addRating(2);
		dummy.addRating(5);

		assertArrayEquals(dummy.getRatings(), new int[] { 0, 1, 0, 0, 2 });
	}
	
	@Test
	public void testAverage() {
		OfflineUser dummy = new OfflineUser("", "", null);
		dummy.addRating(5);
		dummy.addRating(2);
		dummy.addRating(4);

		assertEquals(String.format("%.1f", dummy.getRatingAverage()), String.format("%.1f", (5.0 + 2.0 + 4.0) / 3.0));
	}
	
	@Test
	public void testAddRater() {
		OfflineUser dummy = new OfflineUser("", "", null);
		dummy.addRater("Jimothy");
		
		assertEquals(dummy.getRaters().size(), 2);
	}
	
	@Test
	public void testSetRaters() {
		OfflineUser dummy = new OfflineUser("", "", null);
		dummy.setRaters(new ArrayList<String>(List.of("Bob", "Alice", "Eve", "Juan", "Johnson")));
		
		assertEquals(dummy.getRaters().size(), 5);
	}
}
