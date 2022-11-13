package ru.yandex.praktikum.history;

import ru.yandex.praktikum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    public Node head;
    public Node tail;
    Map<Integer, Node> nodeMap = new HashMap<>();

    @Override
    public void add(Task task) {
        Node node = nodeMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        node = linkLast(task);
        nodeMap.put(task.getId(), node);
    }

    public void removeHistoryAfterRemoveTask(Task task) {
        Node node = nodeMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        //return taskListHistory;
        return getTasks();
    }

    @Override
    public void remove(int id) {
        nodeMap.remove(id);
    }

    public void removeNode(Node node) {
        if (getTasks().size() != 0) {
            if (node.linkPrev == null && node.linkNext == null) {
                head = null;
                tail = null;
            } else if (node.linkPrev == null && node.linkNext != null) {
                node.linkNext.linkPrev = null;
                head = node.linkNext;
            } else if (node.linkPrev != null && node.linkNext == null) {
                node.linkPrev.linkNext = null;
                tail = node.linkPrev;
            } else {
                node.linkPrev.linkNext = node.linkNext;
                node.linkNext.linkPrev = node.linkPrev;
                node.linkNext = null;
                node.linkPrev = null;
            }
        }
        remove(node.task.getId());
    }

    public Node linkLast(Task task) {
        Node node = new Node(null, null, task);
        if (head == null) {
            node = new Node(null, null, task);
            head = node;
            tail = node;
        } else {
            node.linkPrev = tail;
            tail.linkNext = node;
            tail = node;
        }
        return node;

    }

    public List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node node = head;

        while (node != null) {
            historyList.add(node.task);
            node = node.linkNext;
        }

        return historyList;
    }

    static class Node {
        Node linkNext;
        Node linkPrev;
        Task task;

        public Node(Node linkNext, Node linkPrev, Task task) {
            this.linkNext = linkNext;
            this.linkPrev = linkPrev;
            this.task = task;
        }
    }
}