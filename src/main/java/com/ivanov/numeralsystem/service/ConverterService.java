package com.ivanov.numeralsystem.service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ConverterService {
    private final Map<Character, Integer> charToValue = new HashMap<>();
    private final char[] valueToChar = "0123456789ABCDEF".toCharArray();

    public ConverterService() {
        for (int i = 0; i < valueToChar.length; i++) {
            charToValue.put(valueToChar[i], i);
        }
    }

    public String convert(String number, int fromBase, int toBase) {
        validateBases(fromBase, toBase);
        String decimal = convertToDecimal(number, fromBase);
        return convertFromDecimal(decimal, toBase);
    }

    public String convertToDecimal(String number, int fromBase) {
        validateInput(number, fromBase);

        boolean isNegative = number.startsWith("-");
        String numStr = isNegative ? number.substring(1) : number;
        if (numStr.isEmpty()) throw new IllegalArgumentException("Пустая строка числа");

        BigInteger result = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(fromBase);

        for (int i = 0; i < numStr.length(); i++) {
            char c = numStr.charAt(i);
            int value = charToValue.get(c);
            if (value >= fromBase) {
                throw new IllegalArgumentException(
                        "Символ '" + c + "' недопустим для системы с основанием " + fromBase
                );
            }
            result = result.multiply(base).add(BigInteger.valueOf(value));
        }

        return isNegative ? "-" + result : result.toString();
    }

    public String convertFromDecimal(String numberStr, int toBase) {
        validateBase(toBase);

        boolean isNegative = numberStr.startsWith("-");
        String numStr = isNegative ? numberStr.substring(1) : numberStr;
        if (numStr.equals("0")) return "0";

        BigInteger number = new BigInteger(numStr);
        BigInteger base = BigInteger.valueOf(toBase);
        StringBuilder result = new StringBuilder();

        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divRem = number.divideAndRemainder(base);
            result.insert(0, valueToChar[divRem[1].intValue()]);
            number = divRem[0];
        }

        return isNegative ? "-" + result : result.toString();
    }

    public String convertToBinary(String number, int fromBase) {
        String decimal = convertToDecimal(number, fromBase);
        return convertFromDecimal(decimal, 2);
    }

    private void validateBases(int fromBase, int toBase) {
        validateBase(fromBase);
        validateBase(toBase);
    }

    private void validateBase(int base) {
        if (base < 2 || base > 16) {
            throw new IllegalArgumentException("Основание системы должно быть от 2 до 16");
        }
    }

    private void validateInput(String number, int base) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Входное число не может быть пустым");
        }

        String numStr = number.startsWith("-")
                ? number.substring(1)
                : number;

        if (numStr.isEmpty()) {
            throw new IllegalArgumentException("Некорректный формат числа");
        }
    }
}
