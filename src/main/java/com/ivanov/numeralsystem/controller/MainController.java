package com.ivanov.numeralsystem.controller;

import com.ivanov.numeralsystem.service.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.math.BigInteger;

public class MainController {
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

    private final ConverterService converter = new ConverterService();
    private final BitDisplayService bitDisplay = new BitDisplayService();

    @FXML
    public void initialize() {
        fromBase.getItems().addAll("2", "10", "16");
        toBase.getItems().addAll("2", "10", "16");
        calcBase.getItems().addAll("2", "10", "16");
        operation.getItems().addAll("+", "-");

        fromBase.setValue("10");
        toBase.setValue("2");
        calcBase.setValue("10");
        operation.setValue("+");

        setupInputValidation();
    }

    private void setupInputValidation() {
        inputNumber.setTextFormatter(createNumberFilter());
        num1.setTextFormatter(createNumberFilter());
        num2.setTextFormatter(createNumberFilter());

        fromBase.valueProperty().addListener((obs, old, newVal) -> validateConverterInput());
        toBase.valueProperty().addListener((obs, old, newVal) -> validateConverterInput());
        calcBase.valueProperty().addListener((obs, old, newVal) -> validateCalculatorInput());
    }

    private TextFormatter<String> createNumberFilter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText().toUpperCase();
            if (newText.matches("^-?[0-9A-F]*$")) {
                return change;
            }
            return null;
        });
    }

    private void validateConverterInput() {
        try {
            String text = inputNumber.getText();
            int base = Integer.parseInt(fromBase.getValue());
            validateNumber(text, base);
            clearErrorStyle(inputNumber);
        } catch (IllegalArgumentException e) {
            setErrorStyle(inputNumber);
        }
    }

    private void validateCalculatorInput() {
        validateNumberField(num1);
        validateNumberField(num2);
    }

    private void validateNumberField(TextField field) {
        try {
            String text = field.getText();
            int base = Integer.parseInt(calcBase.getValue());
            validateNumber(text, base);
            clearErrorStyle(field);
        } catch (IllegalArgumentException e) {
            setErrorStyle(field);
        }
    }

    private void validateNumber(String number, int base) {
        if (number.isEmpty()) return;

        String pattern = getValidationPattern(base);
        if (!number.matches(pattern)) {
            throw new IllegalArgumentException("Неверный формат числа");
        }
    }

    private String getValidationPattern(int base) {
        switch (base) {
            case 2: return "^-?[01]+$";
            case 10: return "^-?\\d+$";
            case 16: return "^-?[0-9A-F]+$";
            default: return "^-?[0-9A-F]+$";
        }
    }

    private void setErrorStyle(TextField field) {
        field.getStyleClass().add("error-field");
    }

    private void clearErrorStyle(TextField field) {
        field.getStyleClass().remove("error-field");
    }

    @FXML
    private void convertNumber() {
        try {
            String numberStr = inputNumber.getText().trim().toUpperCase();
            String fromStr = fromBase.getValue();
            String toStr = toBase.getValue();

            if (numberStr.isEmpty() || fromStr == null || toStr == null) {
                AlertService.showError("Ошибка", "Заполните все поля конвертера");
                return;
            }

            int from = Integer.parseInt(fromStr);
            int to = Integer.parseInt(toStr);

            validateNumber(numberStr, from);
            String result = converter.convert(numberStr, from, to);
            resultField.setText(result);
            bitDisplay.displayBits(bitContainer, converter.convertToBinary(result, to));

        } catch (NumberFormatException e) {
            AlertService.showError("Ошибка", "Некорректный формат числа");
        } catch (IllegalArgumentException e) {
            AlertService.showError("Ошибка", e.getMessage());
        } catch (Exception e) {
            AlertService.showError("Ошибка", "Непредвиденная ошибка: " + e.getMessage());
        }
    }

    @FXML
    private void calculate() {
        try {
            String num1Str = num1.getText().trim().toUpperCase();
            String num2Str = num2.getText().trim().toUpperCase();
            String baseStr = calcBase.getValue();
            String op = operation.getValue();

            if (num1Str.isEmpty() || num2Str.isEmpty() || baseStr == null || op == null) {
                AlertService.showError("Ошибка", "Заполните все поля калькулятора");
                return;
            }

            int base = Integer.parseInt(baseStr);
            validateNumber(num1Str, base);
            validateNumber(num2Str, base);

            BigInteger n1 = new BigInteger(converter.convertToDecimal(num1Str, base));
            BigInteger n2 = new BigInteger(converter.convertToDecimal(num2Str, base));

            BigInteger result = performOperation(n1, n2, op);
            String resultStr = converter.convertFromDecimal(result.toString(), base);

            calcResult.setText(resultStr);
            bitDisplay.displayBits(calcBitContainer, converter.convertToBinary(resultStr, base));

        } catch (NumberFormatException e) {
            AlertService.showError("Ошибка", "Некорректный формат числа");
        } catch (IllegalArgumentException e) {
            AlertService.showError("Ошибка", e.getMessage());
        } catch (Exception e) {
            AlertService.showError("Ошибка", "Непредвиденная ошибка: " + e.getMessage());
        }
    }

    private BigInteger performOperation(BigInteger n1, BigInteger n2, String op) {
        switch (op) {
            case "+": return n1.add(n2);
            case "-": return n1.subtract(n2);
            default: throw new IllegalArgumentException("Неизвестная операция");
        }
    }
}
