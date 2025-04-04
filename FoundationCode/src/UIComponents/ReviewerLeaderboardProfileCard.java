package UIComponents;

import Application.StartCSE360;
import Application.User;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReviewerLeaderboardProfileCard extends Card {
	
	// A profile card inside the reviewer leaderboard that contains the stats of the reviewer
	public ReviewerLeaderboardProfileCard(User user, Stage primaryStage) {
		super();
		
		// label which contains username as well as a ratings average
		Label titleLabel = new Label(user.getUserName() + " \t" + String.format("%.1f", user.getRatingAverage()));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        int[] ratings = user.getRatings();
        // a text box that holds the individual ratings from other people, from one star to five stars
        Text text = new Text(String.format("1:\t%d\n2:\t%d\n3:\t%d\n4:\t%d\n5:\t%d", ratings[0], ratings[1], ratings[2], ratings[3], ratings[4]));
        text.setStyle("-fx-font-size: 15px;");
        
        // Cannot rate again if already done by the logged in user
        for (String str : user.getRaters()) {
        	if (str.equals(StartCSE360.loggedInUser.getUserName())) {
        		VBox box = new VBox(5, titleLabel, text);
                this.getChildren().addAll(box);
                return;
        	}
        }
        
        // Five buttons that allow the logged in user to create a new rating of the category.
        CustomButton oneButton = new CustomButton("1");
        CustomButton twoButton = new CustomButton("2");
        CustomButton threeButton = new CustomButton("3");
        CustomButton fourButton = new CustomButton("4");
        CustomButton fiveButton = new CustomButton("5");
        
        // one star button, also update the UI accordingly
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
        
        // two star button, also update the UI accordingly
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
        
        // three star button, also update the UI accordingly
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
        
        // four star button, also update the UI accordingly
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
        
        // five star button, also update the UI accordingly
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