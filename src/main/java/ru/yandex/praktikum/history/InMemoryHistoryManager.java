package ru.yandex.praktikum.history;

import ru.yandex.praktikum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (customLinkedList.getNodeMap().get(task.getId()) != null) {
            remove(task.getId());
        }
        customLinkedList.getNodeMap().put(task.getId(), customLinkedList.linkLast(task));
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeNode(id);
        customLinkedList.getNodeMap().remove(id);
    }
}