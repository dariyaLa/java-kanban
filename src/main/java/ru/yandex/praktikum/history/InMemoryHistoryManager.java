package ru.yandex.praktikum.history;

import ru.yandex.praktikum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (customLinkedList.getNodeMap() != null) {           //&& customLinkedList.getNodeMap().get(task.getId()) != null
            removeTask(task.getId());
            if (customLinkedList.getNodeMap() != null) { //если после удаления список снова пуст
                customLinkedList.getNodeMap().put(task.getId(), customLinkedList.linkLast(task));
            }
        }
        if (customLinkedList.getNodeMap() == null) {
            customLinkedList.linkLast(task);
            //customLinkedList.getNodeMap().put(task.getId(), customLinkedList.linkLast(task));
        }

    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void removeTask(int id) {
        if (customLinkedList.getNodeMap().size() !=0 ) {
            if (customLinkedList.getNodeMap().get(id) != null) {
                customLinkedList.removeNode(id);
                if (customLinkedList.getNodeMap() != null) {
                    customLinkedList.getNodeMap().remove(id);
                }
            }
        }
    }
}