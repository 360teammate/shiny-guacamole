package Application;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Database.DatabaseHelper;
import UIPages.FirstPage;
import UIPages.SetupLoginSelectionPage;


public class StartCSE360 extends Application {

	public static final DatabaseHelper databaseHelper = new DatabaseHelper();
	public static QuestionList questions;
	public static AnswerList answers;
	
	public static int WIDTH = 1200;
	public static int HEIGHT = 700;
	
	public static User loggedInUser;
	public static Question currentQuestion;
	
	
	public static void main( String[] args )
	{
		launch(args);
	}
	
	@Override
    public void start(Stage primaryStage) {

        try {
        	
            databaseHelper.connectToDatabase(); // Connect to the database
            questions = new QuestionList(databaseHelper.getQuestions());
			answers = new AnswerList(databaseHelper.getAnswers());
			
			
			if (databaseHelper.isDatabaseEmpty()) {      
				databaseHelper.register(new User("Admin", "adminADMIN1!", new ArrayList<UserRole>(List.of(UserRole.ADMIN))));
				databaseHelper.register(new User("Reviewer", "reviewerREVIEWER1!", new ArrayList<UserRole>(List.of(UserRole.REVIEWER))));
				databaseHelper.register(new User("Instructor", "instructorINSTRUCTOR1!", new ArrayList<UserRole>(List.of(UserRole.INSTRUCTOR))));
				databaseHelper.register(new User("Staff", "staffSTAFF1!", new ArrayList<UserRole>(List.of(UserRole.STAFF))));
				
				databaseHelper.register(new User("Abram", "abramABRAM1!", new ArrayList<UserRole>(List.of(UserRole.STUDENT))));
				databaseHelper.register(new User("Brandon", "brandonBRANDON1!", new ArrayList<UserRole>(List.of(UserRole.STUDENT))));
				databaseHelper.register(new User("Drew", "drewDREW1!", new ArrayList<UserRole>(List.of(UserRole.STUDENT))));
				databaseHelper.register(new User("JoshB", "joshbJOSHB1!", new ArrayList<UserRole>(List.of(UserRole.STUDENT))));
				databaseHelper.register(new User("josh", "OmegaChungus69_", new ArrayList<UserRole>(List.of(UserRole.STUDENT))));
				databaseHelper.register(new User("Shiv", "shivSHIV1!", new ArrayList<UserRole>(List.of(UserRole.STUDENT))));
				
            	new FirstPage(databaseHelper).show(primaryStage);
            } else {
            	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
    }
	

}

