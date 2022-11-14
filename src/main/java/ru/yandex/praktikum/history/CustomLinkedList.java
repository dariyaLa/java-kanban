package ru.yandex.praktikum.history;

import ru.yandex.praktikum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class CustomLinkedList {

    private Node head;
    private Node tail;

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

    public void removeNode(Node node) {
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

    //у меня мапа хранится в другом классе. по заданию сказано - что значение по ключу в мапе - это узел списка, т.е. Node.
    //т.е. я не смогу хранить мапу с узлами в InMemoryHistoryManager.
    //если мапу перемещать в другой класс и Node делать приватным, я не смогу передать ноду из InMemoryHistoryManager для ее удаления из списка.
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