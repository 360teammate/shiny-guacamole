package Application;

import java.sql.SQLException;
import java.util.ArrayList;

import Database.DatabaseHelper;

public class TestRoleRequests {
	private ArrayList<RoleRequest> roleRequests = new ArrayList<>();
	
	TestRoleRequests(DatabaseHelper databaseHelper) throws SQLException{
		roleRequests = databaseHelper.getRoleRequests();
	}
	
	public void printRoleRequests() {
		for(RoleRequest Requests : roleRequests) {
			System.out.println("Request text:" + Requests.getText());
		}
	}
}
