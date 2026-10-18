package ru.university.rahmanina15.exception;

public class NegativeNumberException extends BusinessException {
    public NegativeNumberException(int number) {
        super("Номер выпуска должен быть положительным: " + number);
    }
}
