package application;

import javafx.application.Application;
import javafx.stage.Stage;
import questions.AnswerList;
import questions.PostListUI;
import questions.QuestionList;

import java.sql.SQLException;
import java.util.ArrayList;

import databasePart1.DatabaseHelper;


public class StartCSE360 extends Application {

	public static final DatabaseHelper databaseHelper = new DatabaseHelper();
	public static QuestionList questions;
	public static AnswerList answers;
	
	public static void main( String[] args )
	{
		launch(args);
	}
	
	@Override
    public void start(Stage primaryStage) {
//		try {
//			databaseHelper.connectToDatabase();
//			
//			new PostListUI(questions).show(primaryStage);
//			
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
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