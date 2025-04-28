package com.ivanov.numeralsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    private final Map<Character, Integer> charToValue = new HashMap<>();
    private final char[] valueToChar = "0123456789ABCDEF".toCharArray();

    @FXML private TextField inputNumber;
    @FXML private ComboBox<String> fromBase;
    @FXML private ComboBox<String> toBase;
    @FXML private TextField resultField;
    @FXML private HBox bitContainer;

    @FXML private TextField num1;
    @FXML private TextField num2;
    @FXML private ComboBox<String> calcBase;
    @FXML private ComboBox<String> operation;
    @FXML private TextField calcResult;
    @FXML private HBox calcBitContainer;

    @FXML
    public void initialize() {
        for (int i = 0; i < valueToChar.length; i++) {
            charToValue.put(valueToChar[i], i);
        }


        fromBase.getSelectionModel().select(0);
        toBase.getSelectionModel().select(1);
        calcBase.getSelectionModel().select(0);
        operation.getSelectionModel().select(0);
    }

    @FXML
    private void convertNumber() {
        try {
            String numberStr = inputNumber.getText().trim().toUpperCase();
            int from = Integer.parseInt(fromBase.getValue());
            int to = Integer.parseInt(toBase.getValue());

            if (numberStr.isEmpty()) {
                showAlert("Ошибка", "Введите число для конвертации");
                return;
            }

            String result;
            if (from == 10) {
                result = convertFromDecimal(numberStr, to);
            } else {
                String decimal = convertToDecimal(numberStr, from);
                result = convertFromDecimal(decimal, to);
            }

            resultField.setText(result);

            
            if (to == 2) {
                displayBits(bitContainer, result);
            } else {
                String binaryResult = convertToBinary(result, to);
                displayBits(bitContainer, binaryResult);
            }

        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Некорректный ввод числа");
        } catch (Exception e) {
            showAlert("Ошибка", e.getMessage());
        }
    }

    @FXML
    private void calculate() {
        try {
            String num1Str = num1.getText().trim().toUpperCase();
            String num2Str = num2.getText().trim().toUpperCase();
            int base = Integer.parseInt(calcBase.getValue());
            String op = operation.getValue();

            if (num1Str.isEmpty() || num2Str.isEmpty()) {
                showAlert("Ошибка", "Введите оба числа для вычисления");
                return;
            }

            
            String dec1 = convertToDecimal(num1Str, base);
            String dec2 = convertToDecimal(num2Str, base);

            BigInteger n1 = new BigInteger(dec1);
            BigInteger n2 = new BigInteger(dec2);
            BigInteger result;

            switch (op) {
                case "+":
                    result = n1.add(n2);
                    break;
                case "-":
                    result = n1.subtract(n2);
                    break;
                default:
                    throw new IllegalArgumentException("Неизвестная операция");
            }

            
            String resultStr = convertFromDecimal(result.toString(), base);
            calcResult.setText(resultStr);

            
            displayBits(calcBitContainer, convertToBinary(resultStr, base));

        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Некорректный ввод числа для выбранной системы");
        } catch (Exception e) {
            showAlert("Ошибка", e.getMessage());
        }
    }

    private String convertToDecimal(String number, int fromBase) {
        boolean isNegative = number.startsWith("-");
        String numStr = isNegative ? number.substring(1) : number;

        BigInteger result = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(fromBase);

        for (int i = 0; i < numStr.length(); i++) {
            char c = numStr.charAt(i);
            int value = charToValue.get(c);
            result = result.multiply(base).add(BigInteger.valueOf(value));
        }

        return isNegative ? "-" + result : result.toString();
    }

    private String convertFromDecimal(String numberStr, int toBase) {
        boolean isNegative = numberStr.startsWith("-");
        String numStr = isNegative ? numberStr.substring(1) : numberStr;

        if (numStr.equals("0")) return "0";

        BigInteger number = new BigInteger(numStr);
        BigInteger base = BigInteger.valueOf(toBase);
        StringBuilder result = new StringBuilder();

        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divRem = number.divideAndRemainder(base);
            int remainder = divRem[1].intValue();
            result.insert(0, valueToChar[remainder]);
            number = divRem[0];
        }

        return isNegative ? "-" + result.toString() : result.toString();
    }

    private String convertToBinary(String number, int fromBase) {
        String decimal = convertToDecimal(number, fromBase);
        return convertFromDecimal(decimal, 2);
    }

    private void displayBits(HBox container, String binaryStr) {
        container.getChildren().clear();

        boolean isNegative = binaryStr.startsWith("-");
        String bits = isNegative ? binaryStr.substring(1) : binaryStr;

        
        while (bits.length() < 8) {
            bits = "0" + bits;
        }

        
        if (bits.length() > 8) {
            bits = bits.substring(bits.length() - 8);
        }

        
        createBitBox(container, isNegative ? "1" : "0", true);

        
        for (int i = 0; i < 8; i++) {
            createBitBox(container, String.valueOf(bits.charAt(i)), false);
        }
    }

    private void createBitBox(HBox container, String bitValue, boolean isSignBit) {
        Rectangle bitBox = new Rectangle(30, 30);
        bitBox.getStyleClass().add("bit-box");
        if (isSignBit) {
            bitBox.getStyleClass().add("sign-bit");
        }

        Text bitText = new Text(bitValue);
        bitText.getStyleClass().add("bit-text");

        StackPane bitPane = new StackPane(bitBox, bitText);
        container.getChildren().add(bitPane);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
