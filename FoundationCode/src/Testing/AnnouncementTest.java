package Testing;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import Application.StartCSE360;
import Application.Announcement;
import java.sql.SQLException;
import java.util.List;

public class AnnouncementTest {

    // Setup the database connection before each test
    @Before
    public void setup() throws Exception {
        // Ensure the database connection is properly established before running tests
        if (StartCSE360.databaseHelper != null) {
            StartCSE360.databaseHelper.connectToDatabase();
        } else {
            fail("DatabaseHelper is not initialized.");
        }
    }

    // Test for inserting and deleting an announcement
    @Test
    public void testInsertAndDeleteAnnouncement() {
        try {
            String author = "staff_tester_" + System.currentTimeMillis();
            String content = "This is a test announcement.";
            
            // Insert the announcement
            StartCSE360.databaseHelper.insertAnnouncement(author, content);

            // Confirm the announcement has been inserted by fetching all announcements
            List<Announcement> all = StartCSE360.databaseHelper.getAllAnnouncements();
            boolean exists = all.stream().anyMatch(a -> a.getAuthor().equals(author) && a.getContent().equals(content));
            assertTrue("Announcement should exist after insertion", exists);

            // Get the inserted announcement's ID
            int id = all.stream()
                        .filter(a -> a.getAuthor().equals(author) && a.getContent().equals(content))
                        .findFirst()
                        .get()
                        .getId();

            // Delete the announcement using its ID
            StartCSE360.databaseHelper.deleteAnnouncement(id);

            // Confirm that the announcement has been deleted
            var updated = StartCSE360.databaseHelper.getAllAnnouncements();
            boolean deleted = updated.stream().noneMatch(a -> a.getId() == id);
            assertTrue("Announcement should be deleted after deletion", deleted);

        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}
