package Testing;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import Application.RoleRequest;
import Application.User;
import Application.UserRole;
import Application.StartCSE360;

public class ReviewerRequestTest {

    @Before
    public void setup() throws SQLException {
        if (StartCSE360.databaseHelper != null) {
            StartCSE360.databaseHelper.connectToDatabase();
        }
    }

    @Test
    public void testApproveReviewerRole() {
        try {
            User testUser = new User("testuser1", "password", new ArrayList<>());
            StartCSE360.databaseHelper.register(testUser);
            StartCSE360.databaseHelper.insertRoleRequest("testuser1", "Let me review", UserRole.REVIEWER);

            RoleRequest request = new RoleRequest("testuser1", "Let me review", UserRole.REVIEWER);
            request.approveRequest();

            ArrayList<UserRole> roles = StartCSE360.databaseHelper.getUserRole("testuser1");
            assertTrue(roles.contains(UserRole.REVIEWER));
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDenyReviewerRole() {
        try {
            StartCSE360.databaseHelper.insertRoleRequest("testuser2", "Request denied", UserRole.REVIEWER);
            RoleRequest request = new RoleRequest("testuser2", "Request denied", UserRole.REVIEWER);
            request.rejectRequest();

            ArrayList<RoleRequest> allRequests = StartCSE360.databaseHelper.getRoleRequests();
            boolean found = allRequests.stream().anyMatch(r -> r.getAuthor().equals("testuser2"));
            assertFalse(found);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetPendingRequestsIsNotNull() {
        try {
            ArrayList<User> pending = StartCSE360.databaseHelper.getPendingRequests();
            assertNotNull(pending);
        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleRoleRequestsInserted() {
        try {
            StartCSE360.databaseHelper.removeRoleRequest("alice");
            StartCSE360.databaseHelper.removeRoleRequest("bob");

            StartCSE360.databaseHelper.insertRoleRequest("alice", "I want to review", UserRole.REVIEWER);
            StartCSE360.databaseHelper.insertRoleRequest("bob", "Me too!", UserRole.REVIEWER);

            ArrayList<RoleRequest> requests = StartCSE360.databaseHelper.getRoleRequests();
            boolean hasAlice = requests.stream().anyMatch(r -> r.getAuthor().equals("alice"));
            boolean hasBob = requests.stream().anyMatch(r -> r.getAuthor().equals("bob"));

            assertTrue(hasAlice && hasBob);
        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }


    @Test
    public void testEditRoleRequestText() {
        RoleRequest req = new RoleRequest("Jimbo", "Please?", UserRole.REVIEWER);
        req.editText("Pretty please?");
        assertEquals("Pretty please?", req.getText());
    }
}
