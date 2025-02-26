package application;

import javafx.application.Application;
import javafx.stage.Stage;
import questions.AnswerList;
import questions.PostListUI;
import questions.QuestionList;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;


public class StartCSE360 extends Application {

	public static final DatabaseHelper databaseHelper = new DatabaseHelper();
	public static QuestionList questions;
	public static AnswerList answers;
	
	public static int WIDTH = 1200;
	public static int HEIGHT = 700;
	
	
	public static void main( String[] args )
	{
		launch(args);
	}
	
	@Override
    public void start(Stage primaryStage) {

//		list.newQuestion(
//				"Question about Individual Assignment 2",
//				"From reading all the Ed Discussion posts I have deciphered that we need to decide which user stories apply to CRUD functionality. As far as I am concerned the only two items in the user story that applies strictly to CRUD are:\n"
//				+ "\n"
//				+ "As a student, I can see a list of questions others have asked that might be related to a question I am about to ask, so I do not waste my time waiting for answers already there, and others don’t waste their time reading and answering my question\n"
//				+ "\n"
//				+ "As a student, I can see a list of all unresolved questions and a list of the current potential answers for each so I can evaluate the potential answers and, if appropriate, propose a new potential answer without duplicating the work of others.\n"
//				+ "\n"
//				+ "And then I am implicitly adding a user story (which wasn't included in the document), that the user can modify and delete their questions/answers. This should cover all the bases of CRUD.\n"
//				+ "\n"
//				+ "So I just have three total user stories (for task 1). Two from the document and one that I inferred. Would this be a safe and correct approach? ", 
//				"Abram Pierce");
//		list.newQuestion("Hello World", "Body text", "Author");

		try {
			databaseHelper.connectToDatabase();
			questions = new QuestionList(databaseHelper.getQuestions());
			answers = new AnswerList(databaseHelper.getAnswers());
			new PostListUI(questions).show(primaryStage);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
//        try {
//        	
//            databaseHelper.connectToDatabase(); // Connect to the database
//            if (databaseHelper.isDatabaseEmpty()) {
//            	
//            	new FirstPage(databaseHelper).show(primaryStage);
//            } else {
//            	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
//                
//            }
//        } catch (SQLException e) {
//        	System.out.println(e.getMessage());
//        }
    }
	

}

// abramadmin
// abc123ABC!
// 7e10