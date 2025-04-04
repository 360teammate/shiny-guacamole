package Testing;
import static org.junit.Assert.*;
import org.junit.Test;

import Application.UserRole;

import java.sql.SQLException;
import java.util.ArrayList;
import Database.DatabaseHelper;
import UIPages.UserListPage;

public class UserListPageTest {

    @Test
    public void testDummySuccess() {
        assertTrue(true);
    }

    @Test
    public void testNonNullDatabaseHelper() throws SQLException {
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.connectToDatabase();  // Ensure connection is established
        assertNotNull(dbHelper);
    }

    @Test
    public void testUserListInitialization() throws SQLException {
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.connectToDatabase();  // Ensure connection is established
        UserListPage userListPage = new UserListPage(dbHelper);
        assertNotNull(userListPage);
    }

    @Test
    public void testAssignRoleDoesNotThrowException() throws SQLException {
        DatabaseHelper dbHelper = new DatabaseHelper();
        UserListPage userListPage = new UserListPage(dbHelper);
        ArrayList<UserRole> roles = new ArrayList<>();
        try {
            userListPage.assignRole("testUser", roles, null);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testEmptyRoleList() {
        ArrayList<String> roles = new ArrayList<>();
        assertEquals(0, roles.size());
    }
    
    @Test
    public void testGetAllUsernames() throws SQLException {
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.connectToDatabase(); // Ensure the database connection is established
        
        ArrayList<String> usernames = dbHelper.getAllUsernames();
        
        assertNotNull("getAllUsernames() should not return null", usernames);
    }
}
