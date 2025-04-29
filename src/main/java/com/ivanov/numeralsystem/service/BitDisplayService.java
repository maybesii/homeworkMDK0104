package com.ivanov.numeralsystem.service;

import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;

public class BitDisplayService {
    public void displayBits(HBox container, String binaryStr) {
        container.getChildren().clear();
        boolean isNegative = binaryStr.startsWith("-");
        String bits = isNegative ? binaryStr.substring(1) : binaryStr;

        bits = formatBits(bits);
        createBitBoxes(container, isNegative, bits);
    }

    private String formatBits(String bits) {
        while (bits.length() < 8) bits = "0" + bits;
        if (bits.length() > 8) bits = bits.substring(bits.length() - 8);
        return bits;
    }

    private void createBitBoxes(HBox container, boolean isNegative, String bits) {
        createBitBox(container, isNegative ? "1" : "0", true);
        for (int i = 0; i < 8; i++) {
            createBitBox(container, String.valueOf(bits.charAt(i)), false);
        }
    }

    private void createBitBox(HBox container, String bitValue, boolean isSignBit) {
        Rectangle bitBox = new Rectangle(32, 32);
        bitBox.getStyleClass().add("bit-box");
        if(isSignBit) bitBox.getStyleClass().add("sign-bit");

        Text bitText = new Text(bitValue);
        bitText.getStyleClass().addAll("bit-text", "bit-value-" + bitValue);

        StackPane bitPane = new StackPane(bitBox, bitText);
        container.getChildren().add(bitPane);
    }
}
