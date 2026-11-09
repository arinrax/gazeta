package ru.university.rahmanina15.exception;

public class EmptyNameException extends BusinessException {
    public EmptyNameException() {
        super("Название не может быть пустым");
    }
}
