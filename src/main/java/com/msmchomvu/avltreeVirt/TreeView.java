package com.msmchomvu.avltreeVirt;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class TreeView extends Pane {
    private static final double RADIUS = 20;
    private static final double VERTICAL_GAP = 50;
    private static final double HORIZONTAL_GAP = 20;
    private AVLNode matchedNode = null;  // To keep track of the matched node
    private AVLNode unmatchedNode = null;  // To keep track of the unmatched node

    public void drawTree(AVLNode root, AVLNode matchedNode, AVLNode unmatchedNode) {
        getChildren().clear();
        this.matchedNode = matchedNode;
        this.unmatchedNode = unmatchedNode;
        if (root != null) {
            drawNode(root, getWidth() / 2, VERTICAL_GAP, getWidth() / 4);
        }
    }

    private void drawNode(AVLNode node, double x, double y, double horizontalGap) {
        if (node.left != null) {
            double childX = x - horizontalGap;
            double childY = y + VERTICAL_GAP;
            Line line = new Line(x, y, childX, childY);
            getChildren().add(line);
            drawNode(node.left, childX, childY, horizontalGap / 2);
        }

        if (node.right != null) {
            double childX = x + horizontalGap;
            double childY = y + VERTICAL_GAP;
            Line line = new Line(x, y, childX, childY);
            getChildren().add(line);
            drawNode(node.right, childX, childY, horizontalGap / 2);
        }

        Circle circle = new Circle(x, y, RADIUS);
        if (node == matchedNode) {
            circle.setFill(Color.GREEN);  // Highlight matched node in green
        } else if (node == unmatchedNode) {
            circle.setFill(Color.RED);  // Highlight unmatched nodes in red
        } else {
            circle.setFill(Color.LIGHTBLUE);
        }
        circle.setStroke(Color.BLACK);

        Text text = new Text(x - 8, y + 4, String.valueOf(node.key));

        getChildren().addAll(circle, text);
    }
}
