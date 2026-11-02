package ru.university.rahmanina15.exception;

public class EmptyIzdatException extends BusinessException {
    public EmptyIzdatException() {
        super("Издательство не может быть пустым");
    }
}
