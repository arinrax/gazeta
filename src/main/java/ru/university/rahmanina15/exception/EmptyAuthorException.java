package ru.university.rahmanina15.exception;

public class EmptyAuthorException extends BusinessException {
    public EmptyAuthorException() {
        super("Автор не может быть пустым");
    }
}
