package UIComponents;

import Application.User;
import javafx.scene.control.Label;
// import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReviewerLeaderboardProfileCard extends Card {
	public ReviewerLeaderboardProfileCard(User user, Stage primaryStage) {
		super();
		
		Label titleLabel = new Label(user.getUserName() + "\t" + String.format("%.1f", user.getRatingAverage()));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text text = new Text("1:\t\n2:\t\n3:\t\n4:\t\n5:\t");
        text.setStyle("-fx-font-size: 15px;");
        
        VBox box = new VBox(5, titleLabel, text);
        this.getChildren().addAll(box);
	}
}
