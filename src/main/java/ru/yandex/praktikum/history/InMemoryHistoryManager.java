package ru.yandex.praktikum.history;

import ru.yandex.praktikum.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final int COUNT_TASK_IN_LIST_HISTORY = 10;
    private final List<Task> taskListHistory = new LinkedList<>();
    private final int INDEX_FOR_REMOVE = 0;

    @Override
    public void add(Task task) {
        if (taskListHistory.size() < COUNT_TASK_IN_LIST_HISTORY) {
            taskListHistory.add(task);
        } else {
            taskListHistory.remove(INDEX_FOR_REMOVE);
            taskListHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskListHistory;
    }
}