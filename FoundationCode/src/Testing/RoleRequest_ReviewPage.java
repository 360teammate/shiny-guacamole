package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import Application.Answer;
import Application.RoleRequest;
import Application.User;
import Application.UserRole;
import Database.DatabaseHelper;
import UIPages.ReviewerListPage;
import javafx.stage.Stage;

public class RoleRequest_ReviewPage {

	@Test
	public void roleRequestConstructComplete() {
		RoleRequest testRequest = new RoleRequest("Jimbo", "I love reviewing", UserRole.REVIEWER);
		assertEquals("Jimbo", testRequest.getAuthor());
		assertEquals("I love reviewing", testRequest.getText());
		assertEquals(UserRole.REVIEWER, testRequest.getRequestedRole());
	}
	
	@Test
	public void roleRequestEdit() {
		RoleRequest testRequest = new RoleRequest("Jimbo", "I love reviewing", UserRole.REVIEWER);
		testRequest.editText("I hate reviewing");
		assertEquals("I hate reviewing", testRequest.getText());
	}
	
	
	
}