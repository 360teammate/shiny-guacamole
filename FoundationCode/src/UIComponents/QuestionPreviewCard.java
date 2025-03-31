package UIComponents;

import Application.Question;
import UIPages.PostPage;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class QuestionPreviewCard extends Card {

    public QuestionPreviewCard(Question question, Stage primaryStage) {
        super();

        Label title = new Label(question.getTitle());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;");
        title.setOnMouseEntered(e -> title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;"));
        title.setOnMouseExited(e -> title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;"));
        title.setOnMouseClicked(e -> new PostPage(question).show(primaryStage));

        Label dateLabel = new Label("Posted on " + question.getDateAsString());
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        Label previewText = new Label(trimBodyText(question.getBodyText(), 100));
        previewText.setStyle("-fx-font-size: 14px;");

        Label resolvedLabel = new Label("Resolved ");
        resolvedLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: green; -fx-font-weight: bold;");

        HBox header = new HBox(5);
        header.setAlignment(Pos.CENTER_LEFT);
        if (question.getResolved()) {
            header.getChildren().add(resolvedLabel);
        }
        header.getChildren().add(title);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(spacer, dateLabel);

        this.getChildren().addAll(header, previewText);
    }

    private String trimBodyText(String text, int limit) {
        if (text.length() > limit) return text.substring(0, limit) + "...";
        if (text.contains("\n")) return text.substring(0, text.indexOf("\n")) + "...";
        return text;
    }
}
