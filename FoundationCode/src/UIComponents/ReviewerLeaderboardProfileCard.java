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

        int[] ratings = user.getRatings();
        Text text = new Text(String.format("1:\t%d\n2:\t%d\n3:\t%d\n4:\t%d\n5:\t%d", ratings[0], ratings[1], ratings[2], ratings[3], ratings[4]));
        text.setStyle("-fx-font-size: 15px;");
        
        VBox box = new VBox(5, titleLabel, text);
        this.getChildren().addAll(box);
	}
}
