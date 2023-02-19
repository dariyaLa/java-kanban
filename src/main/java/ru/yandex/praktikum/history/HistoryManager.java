package ru.yandex.praktikum.history;

import ru.yandex.praktikum.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void removeTask(int id);
    List<Task> getHistory();
}