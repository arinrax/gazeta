package ru.university.rahmanina15.exception;

public class NegativePagesException extends BusinessException {
    public NegativePagesException(int pages) {
        super("Количество страниц не может быть отрицательным: " + pages);
    }
}
