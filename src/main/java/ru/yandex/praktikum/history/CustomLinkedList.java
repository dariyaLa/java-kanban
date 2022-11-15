package ru.yandex.praktikum.history;

import ru.yandex.praktikum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList {

    private Node head;
    private Node tail;
    private Map<Integer, Node> nodeMap = new HashMap<>();

    public Node linkLast(Task task) {
        Node node = new Node(null, null, task);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.linkPrev = tail;
            tail.linkNext = node;
            tail = node;
        }
        return node;
    }

    public void removeNode(int id) {
        Node node = nodeMap.get(id);
        Node nodeNext = node.linkNext;
        Node nodePrev = node.linkPrev;

        if (getTasks().size() != 0) {
            if (node.linkPrev == null && node.linkNext == null) {
                head = null;
                tail = null;
            } else if (node.linkPrev == null && node.linkNext != null) {
                nodeNext.linkPrev = null;
                head = node.linkNext;
            } else if (node.linkPrev != null && node.linkNext == null) {
                nodePrev.linkNext = null;
                tail = node.linkPrev;
            } else {
                nodePrev.linkNext = node.linkNext;
                nodeNext.linkPrev = node.linkPrev;
                node.linkNext = null;
                node.linkPrev = null;
            }
        }
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

    public Map<Integer, Node> getNodeMap() {
        return nodeMap;
    }

    private static class Node {
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