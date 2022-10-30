package ru.yandex.praktikum.tasks;

import ru.yandex.praktikum.models.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<SubTask> subTaskList = new ArrayList<>();

    public Epic(String name, String discription, int id, List<SubTask> subTaskList, Enum status) {
        super(name, discription, id, status);
        this.subTaskList = subTaskList;
    }

    public Epic(String name, String discription) {
        super(name, discription);
    }

    public static Epic create(int epicId, Epic epic) {
        return new Epic(epic.name, epic.discription, epicId, new ArrayList<>(), Status.NEW);
    }

    @Override
    public String toString() {
        return "ru.yandex.praktikum.tasks.Epic{" +
                "\n" +
                "name='" + name + '\'' +
                ", discription='" + discription + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}' +
                "\n";
    }

    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }
}