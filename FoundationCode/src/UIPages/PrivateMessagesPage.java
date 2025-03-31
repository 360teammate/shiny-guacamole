package UIPages;

import Application.*;
import UIComponents.CustomButton;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class PrivateMessagesPage {

    private User currentUser = StartCSE360.loggedInUser;

    private HashMap<String, Conversation> conversationMap;
    private ListView<String> conversationListView;
    private VBox messagePane;
    private VBox rootLayout;

    public void show(Stage primaryStage) {
        rootLayout = new VBox(10);
        rootLayout.setPadding(new Insets(10));

        // === NEW CONVERSATION BUTTON ===
        CustomButton newConversationBtn = new CustomButton("New Conversation", CustomButton.ColorPreset.BLUE, e -> {
            showNewConversationDialog(primaryStage);
        });

        // === CONVERSATION LIST ===
        conversationMap = currentUser.getConversations();
        conversationListView = new ListView<>();
        conversationListView.setItems(FXCollections.observableArrayList(conversationMap.keySet()));

        conversationListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Conversation selectedConvo = conversationMap.get(newVal);
                showConversation(selectedConvo);
            }
        });

        CustomButton backBtn = new CustomButton("← Back", CustomButton.ColorPreset.GREY, e -> {
            new UserHomePage().show(primaryStage);
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox controlBar = new HBox(10, backBtn, spacer, newConversationBtn);
        controlBar.setPadding(new Insets(5));

        VBox leftPane = new VBox(10, controlBar, conversationListView);
        leftPane.setPrefWidth(250);


        // === MESSAGE PANE (right side) ===
        messagePane = new VBox(10);
        messagePane.setPadding(new Insets(10));
        VBox.setVgrow(messagePane, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(messagePane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.getItems().addAll(leftPane, scrollPane);
        splitPane.setDividerPositions(0.3);

        rootLayout.getChildren().add(splitPane);

        primaryStage.setScene(new Scene(rootLayout, StartCSE360.WIDTH, StartCSE360.HEIGHT));
        primaryStage.setTitle("Private Messages");
        primaryStage.show();
    }

    private void showConversation(Conversation convo) {
        messagePane.getChildren().clear();
        VBox messages = new VBox(10);

        for (Message msg : convo.getMessages()) {
            VBox msgBox = new VBox(2);
            msgBox.setPadding(new Insets(5, 10, 5, 10));
            msgBox.setStyle("-fx-background-color: #f8f8f8; -fx-background-radius: 6px; -fx-border-color: #e0e0e0; -fx-border-radius: 6px;");

            // Header: Username + timestamp
            String timestampStr = new java.text.SimpleDateFormat("MMM d, h:mm a").format(msg.getTimestamp());
            Label header = new Label(msg.getAuthor() + " • " + timestampStr);
            header.setStyle("-fx-font-size: 12px; -fx-text-fill: #555; -fx-font-weight: bold;");

            // Message Content
            Label body = new Label(msg.getContent());
            body.setWrapText(true);
            body.setStyle("-fx-font-size: 14px; -fx-text-fill: #222;");

            msgBox.getChildren().addAll(header, body);

            // If it's your message, align to right
            HBox wrapper = new HBox(msgBox);
            if (msg.getAuthor().equals(currentUser.getUserName())) {
                wrapper.setStyle("-fx-alignment: center-right;");
                msgBox.setStyle("-fx-background-color: #daf0ff; -fx-background-radius: 6px; -fx-border-color: #c0e0f8; -fx-border-radius: 6px;");
            } else {
                wrapper.setStyle("-fx-alignment: center-left;");
            }

            messages.getChildren().add(wrapper);
        }


        // Create input area container (anchored to bottom)
        TextArea replyField = new TextArea();
        replyField.setPromptText("Type a message...");
        replyField.setWrapText(true);
        replyField.setPrefRowCount(2);

        CustomButton sendBtn = new CustomButton("Send", CustomButton.ColorPreset.BLUE, e -> {
            String content = replyField.getText().trim();
            if (!content.isEmpty()) {
                Message newMessage = new Message(content, currentUser.getUserName(), convo.getUUID());
                convo.getMessages().add(newMessage);
                // TODO: Save to database
                replyField.clear();
                showConversation(convo); // Refresh
            }
        });

        HBox inputBox = new HBox(10, replyField, sendBtn);
        inputBox.setPadding(new Insets(10));
        inputBox.setStyle("-fx-background-color: #f2f2f2;");
        inputBox.setStyle("-fx-alignment: center-right;");
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        messagePane.getChildren().addAll(spacer, messages, inputBox);

    }

    private void showNewConversationDialog(Stage parentStage) {
        Stage dialog = new Stage();
        dialog.initOwner(parentStage);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label instructions = new Label("Search for users to add:");
        TextField searchField = new TextField();
        searchField.setPromptText("Enter username...");

        ListView<String> resultsList = new ListView<>();
        resultsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        resultsList.setPrefHeight(150);

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            // Replace with real user search from DB
            List<String> allUsers = StartCSE360.databaseHelper.getAllUsernames(); // ← you'd implement this
            List<String> matches = allUsers.stream()
                    .filter(u -> u.toLowerCase().contains(newText.toLowerCase()))
                    .filter(u -> !u.equals(currentUser.getUserName()))
                    .toList();
            resultsList.setItems(FXCollections.observableArrayList(matches));
        });

        CustomButton startBtn = new CustomButton("Start Conversation", CustomButton.ColorPreset.BLUE, e -> {
            String selected = resultsList.getSelectionModel().getSelectedItem();
            List<String> selectedUsers = resultsList.getSelectionModel().getSelectedItems();
            if (selectedUsers != null && !selectedUsers.isEmpty()) {
                Set<String> participants = new HashSet<>(selectedUsers);
                participants.add(currentUser.getUserName()); // Always include yourself

                List<String> sorted = new ArrayList<>(participants);
                Collections.sort(sorted);
                String key = String.join(", ", sorted);

                if (!conversationMap.containsKey(key)) {
                    Conversation newConvo = new Conversation(new ArrayList<>(participants));
                    currentUser.newConversation(key, newConvo);
                    conversationMap.put(key, newConvo);
                    conversationListView.getItems().add(key);
                }
                dialog.close();
            }

        });

        layout.getChildren().addAll(instructions, searchField, resultsList, startBtn);
        dialog.setScene(new Scene(layout, 300, 300));
        dialog.setTitle("New Conversation");
        dialog.show();
    }
}
