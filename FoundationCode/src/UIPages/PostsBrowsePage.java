package UIPages;

import java.util.*;

import Application.Question;
import Application.QuestionList;
import Application.StartCSE360;
import UIComponents.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PostsBrowsePage {

    private final QuestionList questionList;

    public PostsBrowsePage(QuestionList questionList) {
        this.questionList = questionList;
    }

    public void show(Stage primaryStage) {
        VBox contentBox = new VBox(10); // Page content layout
        contentBox.setPadding(new Insets(10));

        // === Top Cards ===
        contentBox.getChildren().addAll(
        	    new NavigationCard(primaryStage),
        	    new ReviewerLeaderboardCard(primaryStage),
        	    new CreateQuestionCard(questionList, primaryStage),
        	    new FilterCard(questionList, primaryStage)
        	);

        // === Posts List Card ===
        Card postListCard = new Card();
        Label postListTitle = new Label("All Posts");
        postListTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        postListCard.getChildren().add(postListTitle);

        Map<UUID, Question> questionsToShow = getFilteredOrAllQuestions();

        int count = 0;
        int total = questionsToShow.size();
        for (Question q : questionsToShow.values()) {
            postListCard.getChildren().add(new QuestionPreviewCard(q, primaryStage));
            if (++count < total) {
                postListCard.getChildren().add(new Separator());
            }
        }

        contentBox.getChildren().add(postListCard);

        // === Scrollable Wrapper ===
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));
        scrollPane.setStyle("-fx-background: #f8f9fa; -fx-border-color: transparent;");

        Scene scene = new Scene(scrollPane, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setTitle("Posts");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Map<UUID, Question> getFilteredOrAllQuestions() {
        if (questionList.getFoundQuestions() != null) { // && !questionList.getFoundQuestions().isEmpty()) {
            return questionList.getFoundQuestions();
        } else {
        	questionList.clearFoundQuestions();
            return questionList.getAllQuestions();
        }
    }
}
