package ru.university.rahmanina15.exception;

public class NegativeQuantityException extends BusinessException {
    public NegativeQuantityException(int quantity) {
        super("Количество экземпляров не может быть отрицательным: " + quantity);
    }
}
