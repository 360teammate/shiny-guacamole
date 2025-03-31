package Application;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.SQLException;
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
            	new FirstPage(databaseHelper).show(primaryStage);
            } else {
            	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
    }
	

}

// ACCOUNTS:::

// Username: admin
// Password: adminADMIN111!

// Username: Abram
// Password: abramABRAM111!

// Username: Brandon
// Password: brandonBRANDON111!

// Username: Drew
// Password: drewDREW111!


