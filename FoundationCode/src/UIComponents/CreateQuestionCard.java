package UIComponents;

import Application.StartCSE360;
import Questions.QuestionList;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CreateQuestionCard extends Card {

    public CreateQuestionCard(QuestionList questionList, Stage primaryStage) {
        super();

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

        CustomButton postButton = new CustomButton("Post");
        postButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String body = bodyField.getText().trim();

            if (!title.isEmpty() && !body.isEmpty()) {
                questionList.newQuestion(title, body, StartCSE360.loggedInUser.getUserName());
                new UIPages.PostsBrowsePage(questionList).show(primaryStage);
            }
        });

        this.getChildren().addAll(titleLabel, titleField, bodyField, postButton);
    }
}
