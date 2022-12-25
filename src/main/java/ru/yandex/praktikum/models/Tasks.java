package ru.yandex.praktikum.models;

public enum Tasks {
    TASK,
    EPIC,
    SUBTASK;

    private String task;

    public static Tasks getTask(String task) {
        switch (task) {
            case "TASK":
                return TASK;
            case "EPIC":
                return EPIC;
            case "SUBTASK":
                return SUBTASK;
        }
        return null;
    }
}