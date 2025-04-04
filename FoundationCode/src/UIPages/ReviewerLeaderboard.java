package UIPages;

import java.util.ArrayList;

import Application.StartCSE360;
import Application.User;
import Application.UserRole;
import UIComponents.Card;
import UIComponents.CustomButton;
import UIComponents.ReviewerLeaderboardProfileCard;
// import javafx.application.Platform;
import javafx.scene.Scene;
// import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReviewerLeaderboard {

	public void show(Stage primaryStage) {
		VBox contentBox = new VBox(10);
		contentBox.setStyle("-fx-padding: 30; -fx-background-color: #f4f4f4;");
		
		CustomButton quitButton = new CustomButton("Back to Posts");
        quitButton.setOnAction(event -> {
        	new PostsBrowsePage(StartCSE360.questions).show(primaryStage);
        });

        ArrayList<User> users = StartCSE360.databaseHelper.getUsers();
        ArrayList<Card> c = new ArrayList<>();
        System.out.println(users.size());
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
}
