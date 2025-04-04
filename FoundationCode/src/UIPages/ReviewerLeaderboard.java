package UIPages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Application.StartCSE360;
import Application.User;
import Application.UserRole;
import UIComponents.Card;
import UIComponents.CustomButton;
import UIComponents.ReviewerLeaderboardProfileCard;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReviewerLeaderboard {

	// Reviewer leaderboard page
	public void show(Stage primaryStage) {
		VBox contentBox = new VBox(10);
		contentBox.setStyle("-fx-padding: 30; -fx-background-color: #f4f4f4;");
		
		CustomButton quitButton = new CustomButton("Back to Posts");
        quitButton.setOnAction(event -> {
        	new PostsBrowsePage(StartCSE360.questions).show(primaryStage);
        });

        // Retrieve reviewer profiles from the database
        ArrayList<String> userNames = StartCSE360.databaseHelper.getAllUsernames();
        ArrayList<String> reviewerNames = new ArrayList<String>();
        ArrayList<User> users = new ArrayList<User>();
        for (String name : userNames) {
        	if (StartCSE360.databaseHelper.getUserRole(name).contains(UserRole.REVIEWER)) {
        		reviewerNames.add(name);
        	}
        }
        // Create a list of reviewer objects
        for (String name : reviewerNames) {
        	User nU = new User(name, "", null);
        	nU.setRaters(StartCSE360.databaseHelper.getRaters(name));
        	nU.setRatings(StartCSE360.databaseHelper.getRatings(name));
        	users.add(nU);
        }
        
        // Sort the list with the private comparator
        Collections.sort(users, new SortByRating());
        
        ArrayList<Card> c = new ArrayList<>();
        // Create a profile card for each user
        for (User u : users) {
        	c.add(new ReviewerLeaderboardProfileCard(u, primaryStage));
        }
        
        contentBox.getChildren().addAll(quitButton);
        for (Card card : c) {
        	contentBox.getChildren().add(card);
        }

        Scene userScene = new Scene(contentBox, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setScene(userScene);
        primaryStage.setTitle("Reviewer Leaderboard");
	}
	
	// A custom comparator to sort the reviewers by their rating average, from highest to lowest
	private class SortByRating implements Comparator<User> {
		@Override
		public int compare(User u1, User u2) {
			return (int)(10 * (u2.getRatingAverage() - u1.getRatingAverage()));
		}
	}
}
