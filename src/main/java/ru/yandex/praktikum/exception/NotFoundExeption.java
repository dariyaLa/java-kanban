package ru.yandex.praktikum.exception;

import java.io.IOException;

public class NotFoundExeption  extends Throwable{
    String message;

    public NotFoundExeption() {
        message = "Сущность с таким id не найдена, не создана или удалена";
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
