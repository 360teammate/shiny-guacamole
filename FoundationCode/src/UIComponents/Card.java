package UIComponents;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class Card extends VBox {

    public Card(Node... children) {
        super(10); // Spacing between children
        this.getChildren().addAll(children);
        this.setPadding(new Insets(15));
        this.setStyle("-fx-background-color: white; " +
                      "-fx-border-color: #ddd; " +
                      "-fx-border-radius: 8px; " +
                      "-fx-background-radius: 8px; " +
                      "-fx-padding: 15px;");
        this.setMaxWidth(Double.MAX_VALUE);
    }
}
