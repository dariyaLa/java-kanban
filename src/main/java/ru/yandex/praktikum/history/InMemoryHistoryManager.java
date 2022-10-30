package ru.yandex.praktikum.history;

import ru.yandex.praktikum.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final int COUNT_TASK_IN_LIST_HISTORY = 10;
    private final List<Task> taskListHistory = new LinkedList<>();
    private final int INDEX_FOR_ADD = 0;

    //из ТЗ нужно удалять именно начало списка
    //"Если размер списка исчерпан, из него нужно удалить самый старый элемент — тот который находится в начале списка"
    @Override
    public void add(Task task) {
        if (taskListHistory.size() < COUNT_TASK_IN_LIST_HISTORY) {
            taskListHistory.add(task);
        } else {
            taskListHistory.remove(INDEX_FOR_ADD);
            taskListHistory.add(INDEX_FOR_ADD, task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskListHistory;
    }
}