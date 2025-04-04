package UIComponents;

import java.nio.channels.NetworkChannel;

import Application.StartCSE360;
import Application.User;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReviewerLeaderboardProfileCard extends Card {
	public ReviewerLeaderboardProfileCard(User user, Stage primaryStage) {
		super();
		
		Label titleLabel = new Label(user.getUserName() + " \t" + String.format("%.1f", user.getRatingAverage()));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        int[] ratings = user.getRatings();
        Text text = new Text(String.format("1:\t%d\n2:\t%d\n3:\t%d\n4:\t%d\n5:\t%d", ratings[0], ratings[1], ratings[2], ratings[3], ratings[4]));
        text.setStyle("-fx-font-size: 15px;");
        
        
        for (String str : user.getRaters()) {
        	if (str.equals(StartCSE360.loggedInUser.getUserName())) {
        		VBox box = new VBox(5, titleLabel, text);
                this.getChildren().addAll(box);
                return;
        	}
        }
        
        CustomButton oneButton = new CustomButton("1");
        CustomButton twoButton = new CustomButton("2");
        CustomButton threeButton = new CustomButton("3");
        CustomButton fourButton = new CustomButton("4");
        CustomButton fiveButton = new CustomButton("5");
        
        oneButton.setOnAction(event -> {
        	user.addRating(1);
        	user.addRater(user.getUserName());
        	titleLabel.setText(user.getUserName() + " \t" + String.format("%.1f", user.getRatingAverage()));
        	text.setText(String.format("1:\t%d\n2:\t%d\n3:\t%d\n4:\t%d\n5:\t%d", ratings[0], ratings[1], ratings[2], ratings[3], ratings[4]));
        	user.addRater(StartCSE360.loggedInUser.getUserName());
        	oneButton.setVisible(false);
        	twoButton.setVisible(false);
        	threeButton.setVisible(false);
            fourButton.setVisible(false);
            fiveButton.setVisible(false);
        });
        
        
        twoButton.setOnAction(event -> {
        	user.addRating(2);
        	titleLabel.setText(user.getUserName() + " \t" + String.format("%.1f", user.getRatingAverage()));
        	text.setText(String.format("1:\t%d\n2:\t%d\n3:\t%d\n4:\t%d\n5:\t%d", ratings[0], ratings[1], ratings[2], ratings[3], ratings[4]));
        	user.addRater(StartCSE360.loggedInUser.getUserName());
        	oneButton.setVisible(false);
        	twoButton.setVisible(false);
        	threeButton.setVisible(false);
            fourButton.setVisible(false);
            fiveButton.setVisible(false);
        });
        
        threeButton.setOnAction(event -> {
        	user.addRating(3);
        	titleLabel.setText(user.getUserName() + " \t" + String.format("%.1f", user.getRatingAverage()));
        	text.setText(String.format("1:\t%d\n2:\t%d\n3:\t%d\n4:\t%d\n5:\t%d", ratings[0], ratings[1], ratings[2], ratings[3], ratings[4]));
        	user.addRater(StartCSE360.loggedInUser.getUserName());
        	oneButton.setVisible(false);
        	twoButton.setVisible(false);
        	threeButton.setVisible(false);
            fourButton.setVisible(false);
            fiveButton.setVisible(false);
        });
        
        fourButton.setOnAction(event -> {
        	user.addRating(4);
        	titleLabel.setText(user.getUserName() + " \t" + String.format("%.1f", user.getRatingAverage()));
        	text.setText(String.format("1:\t%d\n2:\t%d\n3:\t%d\n4:\t%d\n5:\t%d", ratings[0], ratings[1], ratings[2], ratings[3], ratings[4]));
        	user.addRater(StartCSE360.loggedInUser.getUserName());
        	oneButton.setVisible(false);
        	twoButton.setVisible(false);
        	threeButton.setVisible(false);
            fourButton.setVisible(false);
            fiveButton.setVisible(false);
        });

        fiveButton.setOnAction(event -> {
        	user.addRating(5);
        	titleLabel.setText(user.getUserName() + " \t" + String.format("%.1f", user.getRatingAverage()));
        	text.setText(String.format("1:\t%d\n2:\t%d\n3:\t%d\n4:\t%d\n5:\t%d", ratings[0], ratings[1], ratings[2], ratings[3], ratings[4]));
        	user.addRater(StartCSE360.loggedInUser.getUserName());
        	oneButton.setVisible(false);
        	twoButton.setVisible(false);
        	threeButton.setVisible(false);
            fourButton.setVisible(false);
            fiveButton.setVisible(false);
        });
        
        VBox box = new VBox(5, titleLabel, text);
        HBox hbox = new HBox(10, oneButton, twoButton, threeButton, fourButton, fiveButton);
        this.getChildren().addAll(box, hbox);
	}
}
