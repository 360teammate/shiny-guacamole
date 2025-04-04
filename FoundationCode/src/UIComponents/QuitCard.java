package UIComponents;

import Application.StartCSE360;
import UIPages.SetupLoginSelectionPage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class QuitCard extends Card {
	public QuitCard(Stage primaryStage) {
		super();
		
        CustomButton button = new CustomButton("Quit to Login", CustomButton.ColorPreset.RED);
        button.setOnAction(event -> {
            new SetupLoginSelectionPage(StartCSE360.databaseHelper).show(primaryStage);
        });
        
        HBox box = new HBox(10, button);
        this.getChildren().addAll(box);
	}
}
