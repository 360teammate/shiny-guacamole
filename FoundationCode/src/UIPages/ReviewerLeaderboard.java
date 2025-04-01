package UIPages;

import Application.StartCSE360;
import UIComponents.CustomButton;
// import javafx.application.Platform;
import javafx.scene.Scene;
// import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReviewerLeaderboard {

	public void show(Stage primaryStage) {
		VBox contentBox = new VBox(10);
		CustomButton quitButton = new CustomButton("Back to Posts");
        quitButton.setOnAction(event -> {
        	new PostsBrowsePage(StartCSE360.questions).show(primaryStage);
        });

        contentBox.getChildren().addAll(quitButton);

        Scene userScene = new Scene(contentBox, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setScene(userScene);
        primaryStage.setTitle("Reviewer Leaderboard");
	}
}
