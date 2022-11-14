package ru.yandex.praktikum.history;

import ru.yandex.praktikum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    public CustomLinkedList customLinkedList = new CustomLinkedList();
    public Map<Integer, CustomLinkedList.Node> nodeMap = new HashMap<>();

    @Override
    public void add(Task task) {
        CustomLinkedList.Node node = nodeMap.get(task.getId());
        if (node != null) {
            remove(task.getId());
        }
        node = customLinkedList.linkLast(task);
        nodeMap.put(task.getId(), node);
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        CustomLinkedList.Node node = nodeMap.get(id);
        if (node != null) {
            customLinkedList.removeNode(node);
            nodeMap.remove(id);
        }
    }
}