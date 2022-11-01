package ru.yandex.praktikum.history;

import ru.yandex.praktikum.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final int COUNT_TASK_IN_LIST_HISTORY = 11;
    private final List<Task> taskListHistory = new LinkedList<>();
    private final int INDEX_FOR_REMOVE = 0;

    //нельзя тут справится без else
    @Override
    public void add(Task task) {
        taskListHistory.add(task);
        if (taskListHistory.size() > COUNT_TASK_IN_LIST_HISTORY) {
            taskListHistory.remove(INDEX_FOR_REMOVE);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskListHistory;
    }
}