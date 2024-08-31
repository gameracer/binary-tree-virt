package com.msmchomvu.avltreeVirt;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class AVLTree {
    private AVLNode root;
    private AVLNode searchedNode;
    private AVLNode rotationNode1 = null;
    private AVLNode rotationNode2 = null;
    // Utility functions to manage the AVL tree, like insert, delete, and search
    private int height(AVLNode N) {
        return N == null ? 0 : N.height;
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        // Highlight rotation nodes
        rotationNode1 = y;
        rotationNode2 = x;
        highlightAndWait();

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        // Return new root
        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        // Highlight rotation nodes
        rotationNode1 = x;
        rotationNode2 = y;
        highlightAndWait();

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        // Return new root
        return y;
    }

    private int getBalance(AVLNode N) {
        return (N == null) ? 0 : height(N.left) - height(N.right);
    }
    // Utility to highlight and wait (simulate slow motion)
    private void highlightAndWait() {
        // This method will highlight the nodes involved in the rotation
        // and wait for 1 second to simulate slow motion
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            // After waiting, the rotation will be applied
            rotationNode1 = null;
            rotationNode2 = null;
        });
        pause.play();
    }

    public AVLNode insert(AVLNode node, int key) {
        // Perform the normal BST insertion
        if (node == null)
            return (new AVLNode(key));

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else
            return node;

        // Update height
        node.height = 1 + max(height(node.left), height(node.right));

        // Get balance factor
        int balance = getBalance(node);

        // If the node becomes unbalanced, perform rotations
        if (balance > 1 && key < node.left.key) {
            return rightRotate(node); // Left Left Case
        }
        if (balance < -1 && key > node.right.key) {
            return leftRotate(node); // Right Right Case
        }
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node); // Left Right Case
        }
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node); // Right Left Case
        }

        return node;
    }

    public void insert(int key) {
        root = insert(root, key);
    }

    //delete
    public AVLNode delete(AVLNode root, int key) {
        // Perform standard BST delete
        if (root == null)
            return root;

        if (key < root.key)
            root.left = delete(root.left, key);
        else if (key > root.key)
            root.right = delete(root.right, key);
        else {
            // Node with only one child or no child
            if ((root.left == null) || (root.right == null)) {
                AVLNode temp = (root.left != null) ? root.left : root.right;

                // No child case
                if (temp == null) {
                    temp = root;
                    root = null;
                } else // One child case
                    root = temp;
            } else {
                // Node with two children: Get the inorder successor
                AVLNode temp = minValueNode(root.right);

                // Copy the inorder successor's data to this node
                root.key = temp.key;

                // Delete the inorder successor
                root.right = delete(root.right, temp.key);
            }
        }

        // If the tree had only one node then return
        if (root == null)
            return root;

        // Update height of the current node
        root.height = max(height(root.left), height(root.right)) + 1;

        // Get the balance factor of this node
        int balance = getBalance(root);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;

        while (current.left != null)
            current = current.left;

        return current;
    }

    public void delete(int key) {
        root = delete(root, key);
    }
    // Search Function
    public AVLNode search(AVLNode node, int key) {
        if (node == null || node.key == key) {
            searchedNode = node;
            return node;
        }

        if (key < node.key)
            return search(node.left, key);

        return search(node.right, key);
    }

    public boolean search(int key) {
        searchedNode = null;
        AVLNode result = search(root, key);
        return result != null;
    }

    public AVLNode getRoot() {
        return root;
    }

    public AVLNode getSearchedNode() {
        return searchedNode;
    }

}
