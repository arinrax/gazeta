package ru.university.rahmanina15.exception;

public class NotInStockException extends BusinessException {
    public NotInStockException(int requested, int available) {
        super("Недостаточно товара: запрошено " + requested + ", в наличии " + available);
    }
}
