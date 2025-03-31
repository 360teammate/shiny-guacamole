package UIPages;

import java.util.UUID;

import Application.Question;
import Application.StartCSE360;
import UIComponents.QuestionCard;
import UIComponents.ReplyThreadCard;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PostPage {
    private Question question;
    private VBox replyContainer;

    public PostPage(Question question) {
        this.question = question;
    }

    public void show(Stage primaryStage) {
        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(10));

        replyContainer = new VBox(10);
        loadReplies();

        QuestionCard questionCard = new QuestionCard(question, primaryStage, this::loadReplies);

        contentBox.getChildren().addAll(questionCard, replyContainer);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        Scene scene = new Scene(scrollPane, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setTitle(question.getTitle());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadReplies() {
        if (replyContainer == null) return;
        replyContainer.getChildren().clear();

        for (UUID answerID : question.getChildren()) {
            replyContainer.getChildren().add(new ReplyThreadCard(StartCSE360.answers.getAnswer(answerID), question, this::loadReplies));
        }
    }
}
