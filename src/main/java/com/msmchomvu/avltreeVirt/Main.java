package com.msmchomvu.avltreeVirt;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private AVLTree tree = new AVLTree();
    private TreeView treeView = new TreeView();
    private boolean slowMotion = false;
    private Text message = new Text();  // Message label for showing not found in red

    @Override
    public void start(Stage primaryStage) {
        TextField tfKey = new TextField();
        tfKey.setPromptText("Enter numbers separated by commas");
        tfKey.setPrefColumnCount(20);  // Adjust size for multiple inputs

        Button btnInsert = new Button("Insert");
        Button btnDelete = new Button("Delete");
        Button btnSearch = new Button("Search");

        CheckBox cbSlowMotion = new CheckBox("Enable Slow Motion");

        HBox hBox = new HBox(10, tfKey, btnInsert, btnDelete, btnSearch, cbSlowMotion);
        hBox.setStyle("-fx-alignment: center");

        BorderPane pane = new BorderPane();
        pane.setCenter(treeView);

        // Create a label for creator details
        Text creatorDetails = new Text("Created by Mussa Salim Mchomvu(12326986) from Lovely Professional University(India)");
        creatorDetails.setStyle("-fx-font-size: 18px; -fx-text-fill: gray;");

        // VBox layout to include everything at the bottom (input, message, and creator details)
        VBox vbox = new VBox(10, message, hBox, creatorDetails);
        vbox.setStyle("-fx-alignment: center");
        pane.setBottom(vbox);

        // Toggle slow motion mode
        cbSlowMotion.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            slowMotion = isSelected;
        });

        // Insert button handler - accepts multiple values
        btnInsert.setOnAction(e -> {
            try {
                String input = tfKey.getText();
                String[] values = input.split(",");

                for (String value : values) {
                    tree.insert(Integer.parseInt(value.trim()));
                }
                message.setText("");  // Clear any previous messages
                treeView.drawTree(tree.getRoot(), null, null);  // Redraw tree without highlight
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input");
            }
        });

        // Delete button handler - delete single value
        btnDelete.setOnAction(e -> {
            try {
                int key = Integer.parseInt(tfKey.getText().trim());
                tree.delete(key);
                message.setText("");  // Clear any previous messages
                treeView.drawTree(tree.getRoot(), null, null);  // Redraw tree without highlight
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input");
            }
        });

        // Search button handler - search single value with optional slow motion
        btnSearch.setOnAction(e -> {
            try {
                int key = Integer.parseInt(tfKey.getText().trim());
                if (slowMotion) {
                    message.setText("");  // Clear any previous messages
                    searchWithDelay(key);
                } else {
                    boolean found = tree.search(key);
                    if (found) {
                        treeView.drawTree(tree.getRoot(), tree.getSearchedNode(), null);  // Redraw tree with highlight
                    } else {
                        treeView.drawTree(tree.getRoot(), null, null);  // No highlight if not found
                        message.setFill(Color.RED);
                        message.setText("Node not found");
                        message.setStyle("-fx-font-size: 24px");
                    }
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input");
            }
        });

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("AVL Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Slow-motion search function with delay
    private void searchWithDelay(int key) {
        new Thread(() -> {
            AVLNode currentNode = tree.getRoot();
            while (currentNode != null) {
                final AVLNode node = currentNode;
                PauseTransition pause = new PauseTransition(Duration.seconds(1));

                if (node.key == key) {
                    // Highlight matched node in green
                    pause.setOnFinished(event -> treeView.drawTree(tree.getRoot(), node, null));
                    pause.play();
                    message.setText("");  // Clear any previous messages
                    break;
                } else {
                    // Highlight unmatched nodes in red
                    pause.setOnFinished(event -> treeView.drawTree(tree.getRoot(), null, node));
                    pause.play();
                }

                // Move to the next node
                currentNode = (key < node.key) ? node.left : node.right;

                try {
                    Thread.sleep(1000);  // Wait 1 second between steps
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            if (currentNode == null) {
                // Display "Node not found" message in red
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    message.setFill(Color.RED);
                    message.setText("Node not found");
                });
                pause.play();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}