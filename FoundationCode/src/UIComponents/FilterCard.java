package UIComponents;

import Questions.QuestionList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class FilterCard extends Card {

    public FilterCard(QuestionList questionList, Stage primaryStage) {
        super();

        Label titleLabel = new Label("Filter Posts");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField searchTermField = new TextField();
        searchTermField.setPromptText("Enter search term...");

        TextField searchUserField = new TextField();
        searchUserField.setPromptText("By user...");

        ComboBox<String> resolvedFilterComboBox = new ComboBox<>();
        resolvedFilterComboBox.getItems().addAll("[Resolved Status]", "Resolved", "Unresolved");
        resolvedFilterComboBox.getSelectionModel().select(0);

        CustomButton resetButton = new CustomButton("Reset Filters", CustomButton.ColorPreset.RED);
        resetButton.setOnAction(event -> {
            searchTermField.clear();
            searchUserField.clear();
            resolvedFilterComboBox.getSelectionModel().select(0);
            questionList.clearFoundQuestions();
            new UIPages.PostsBrowsePage(questionList).show(primaryStage);
        });

        CustomButton applyButton = new CustomButton("Apply");
        applyButton.setOnAction(event -> {
            String term = searchTermField.getText();
            String author = searchUserField.getText();
            Boolean state = null;
            String selection = resolvedFilterComboBox.getValue();
            if ("Resolved".equals(selection)) state = true;
            else if ("Unresolved".equals(selection)) state = false;

            questionList.searchAll(term, author, state);
            new UIPages.PostsBrowsePage(questionList).show(primaryStage);
        });

        HBox filtering = new HBox(10, resolvedFilterComboBox, resetButton, applyButton);

        this.getChildren().addAll(titleLabel, searchTermField, searchUserField, filtering);
    }
}
