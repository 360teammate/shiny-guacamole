package UIComponents;

import Application.StartCSE360;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AnnouncementCard extends Card {

    public AnnouncementCard() {
        setPadding(new Insets(15));
        setSpacing(10);
        setAlignment(Pos.TOP_LEFT);

        Label title = new Label("📢 Latest Announcement");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        String announcementText = StartCSE360.databaseHelper.getLatestAnnouncement();
        Label content = new Label(
            (announcementText == null || announcementText.isBlank())
                ? "No announcements yet."
                : announcementText
        );
        content.setWrapText(true);
        content.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        getChildren().addAll(title, content);
        this.setStyle("-fx-background-color: #fffbe6; -fx-border-color: #ffecb3; -fx-border-radius: 5; -fx-background-radius: 5;");

    }
}
