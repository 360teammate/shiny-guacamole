package UIComponents;

import UIComponents.CustomButton.ColorPreset;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

// import javafx.scene.control.Label;

public class ReviewerLeaderboardCard extends Card {
	
	// A card in the posts list page that takes the user to the reviewer leaderboard
	public ReviewerLeaderboardCard(Stage primaryStage) {
		super();
		
		/*
		Label titleLabel = new Label("See Reviewer Leaderboard");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        */
        CustomButton button = new CustomButton("See Reviewer Leaderboard", ColorPreset.BLUE);
        button.setOnAction(event -> {
            new UIPages.ReviewerLeaderboard().show(primaryStage);
        });
        
        HBox box = new HBox(10, button);
        this.getChildren().addAll(box);
	}
}
