package com.msmchomvu.avltreeVirt;

public class AVLNode {
    int key, height;
    AVLNode left, right;

    public AVLNode(int d) {
        key = d;
        height = 1;
    }
}
