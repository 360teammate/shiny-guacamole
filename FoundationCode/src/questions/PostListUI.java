package questions;

import java.util.*;

import application.StartCSE360;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PostListUI {
    private QuestionList questionList;

    public PostListUI(QuestionList questionList) {
        this.questionList = questionList;
    }

    public void show(Stage primaryStage) {
        VBox contentBox = new VBox();
        contentBox.setSpacing(10); // Space between sections

        // ** New Post Creation Card **
        VBox newPostCard = createNewPostCard(primaryStage);
        contentBox.getChildren().add(newPostCard);

        VBox filterCard = filterPostCard(primaryStage);
        contentBox.getChildren().add(filterCard);
        
        // ** Post List Wrapped in a Card **
        VBox postListCard = new VBox();
        postListCard.setSpacing(10);
        postListCard.setPadding(new Insets(10));
        postListCard.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");
        postListCard.setMaxWidth(Double.MAX_VALUE);

        Label postListTitle = new Label("All Posts");
        postListTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        postListCard.getChildren().add(postListTitle);

        if (this.questionList.getFoundQuestions() != null && this.questionList.getFoundQuestions().size() > 0) {
        	int count = 0;
            for (Question question : this.questionList.getFoundQuestions().values()) {
                VBox postCard = createPostCard(question, primaryStage);
                postListCard.getChildren().add(postCard);

                // Add a horizontal separator after each post, except the last one
                if (count < this.questionList.getAllQuestions().size() - 1) {
                    postListCard.getChildren().add(new Separator());
                }
                count++;
            }
        }
        else {
        	int count = 0;
            for (Question question : this.questionList.getAllQuestions().values()) {
                VBox postCard = createPostCard(question, primaryStage);
                postListCard.getChildren().add(postCard);

                // Add a horizontal separator after each post, except the last one
                if (count < this.questionList.getAllQuestions().size() - 1) {
                    postListCard.getChildren().add(new Separator());
                }
                count++;
            }
        }

        contentBox.getChildren().add(postListCard);

        // ScrollPane wraps VBox to allow scrolling
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));
        scrollPane.setStyle("-fx-background: #f8f9fa; -fx-border-color: transparent;");

        // Scene Setup
        Scene scene = new Scene(scrollPane, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setTitle("Post List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox filterPostCard(Stage primaryStage) {
    	Label titleLabel = new Label("Filter Posts");
    	titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    	TextField searchTermField = new TextField();
    	searchTermField.setPromptText("Enter search term...");
    	searchTermField.setMaxWidth(Double.MAX_VALUE);
    	
    	TextField searchUserField = new TextField();
    	searchUserField.setPromptText("By user...");
    	searchUserField.setMaxWidth(Double.MAX_VALUE);
    	
    	ComboBox<String> resolvedFilterComboBox = new ComboBox<>();
    	resolvedFilterComboBox.getItems().addAll(
    		"[Resolved Status]",
    		"Resolved",
    		"Unresolved"
    	);
    	
    	resolvedFilterComboBox.getSelectionModel().select(0);
    	resolvedFilterComboBox.setOnAction(event -> {
    		// nothing probably
    	});
    	
    	Button resetButton = new Button("Reset Filters");
    	resetButton.setStyle("-fx-background-color: #CE0000; -fx-text-fill: white; -fx-border-radius: 5px;");
    	resetButton.setOnMouseEntered(e -> resetButton.setStyle("-fx-background-color: #B90000; -fx-text-fill: white; -fx-border-radius: 5px;"));
    	resetButton.setOnMouseExited(e -> resetButton.setStyle("-fx-background-color: #CE0000; -fx-text-fill: white; -fx-border-radius: 5px;"));
    	
    	resetButton.setOnAction(event -> {
    		searchTermField.clear();
    		searchUserField.clear();
    		resolvedFilterComboBox.getSelectionModel().select(0);
    		questionList.clearFoundQuestions();
    		new PostListUI(questionList).show(primaryStage);
    	});
    	
    	Button applyButton = new Button("Apply");
    	applyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px;");
    	applyButton.setOnMouseEntered(e -> applyButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-border-radius: 5px;"));
    	applyButton.setOnMouseExited(e -> applyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px;"));
    	
    	applyButton.setOnAction(event -> {
    		String term = searchTermField.getText();
    		String author = searchUserField.getText();
    		Boolean state = null;
    		if (resolvedFilterComboBox.getValue().equals("Resolved")) {
    			state = true;
    		}
    		else if (resolvedFilterComboBox.getValue().equals("Unresolved")) {
    			state = false;
    		}
    		// DEbUg
    		HashMap<UUID, Question> result = questionList.searchAll(term, author, state);
    		System.out.println(result.size());
    		new PostListUI(questionList).show(primaryStage);
    	});
    	
    	VBox newPostCard = new VBox(10, titleLabel, searchTermField, searchUserField, resolvedFilterComboBox, resetButton, applyButton);
        newPostCard.setPadding(new Insets(10));
        newPostCard.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");
        newPostCard.setMaxWidth(Double.MAX_VALUE);
        
        return newPostCard;
    }
    
    // ** Creates a new post creation card **
    private VBox createNewPostCard(Stage primaryStage) {
        Label titleLabel = new Label("Create a New Post");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField titleField = new TextField();
        titleField.setPromptText("Enter post title...");
        titleField.setMaxWidth(Double.MAX_VALUE);

        TextArea bodyField = new TextArea();
        bodyField.setPromptText("Enter post body...");
        bodyField.setWrapText(true);
        bodyField.setPrefRowCount(3);
        bodyField.setMaxWidth(Double.MAX_VALUE);

        Button postButton = new Button("Post");
        postButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px;");
        postButton.setOnMouseEntered(e -> postButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-border-radius: 5px;"));
        postButton.setOnMouseExited(e -> postButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px;"));

        postButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String body = bodyField.getText().trim();

            if (!title.isEmpty() && !body.isEmpty()) {
                questionList.newQuestion(title, body, "User123");
                new PostListUI(questionList).show(primaryStage);
            }
        });

        VBox newPostCard = new VBox(10, titleLabel, titleField, bodyField, postButton);
        newPostCard.setPadding(new Insets(10));
        newPostCard.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");
        newPostCard.setMaxWidth(Double.MAX_VALUE);

        return newPostCard;
    }

    // ** Creates a styled post card with a checkmark for resolved posts **
    private VBox createPostCard(Question question, Stage primaryStage) {
        Label title = new Label(question.getTitle());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;");
        title.setOnMouseEntered(e -> title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;"));
        title.setOnMouseExited(e -> title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;"));

        title.setOnMouseClicked(e -> {
            System.out.println("Title clicked: " + question.getTitle());
            new PostUI(question).show(primaryStage);
        });

        Label dateLabel = new Label("Posted on " + question.getDateAsString());
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        Label previewText = new Label(trimBodyText(question.getBodyText(), 100)); // Show preview of body text
        previewText.setStyle("-fx-font-size: 14px;");
        
        Label checkmark = new Label("Resolved ");
        checkmark.setStyle("-fx-font-size: 18px; -fx-text-fill: green; -fx-font-weight: bold;");
        
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        if (question.getResolved()) {
        	header.getChildren().add(checkmark);
        }

        header.getChildren().add(title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(spacer, dateLabel);

        VBox postCard = new VBox(5, header, previewText);
        postCard.setPadding(new Insets(10));
        postCard.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");
        postCard.setMaxWidth(Double.MAX_VALUE);

        return postCard;
    }

    // ** Trims body text for preview display **
    private String trimBodyText(String text, int limit) {
        if (text.length() > limit) {
            return text.substring(0, limit) + "...";
        } else if (text.indexOf("\n") != -1 && text.indexOf("\n") < text.length()) {
        	return text.substring(0, text.indexOf("\n")) + "...";
        }
        return text;
    }
}
