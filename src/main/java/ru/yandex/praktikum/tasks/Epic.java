package ru.yandex.praktikum.tasks;

import ru.yandex.praktikum.models.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<SubTask> subTaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String discription, int id, List<SubTask> subTaskList, Enum status, LocalDateTime startTime, Duration duration) {
        super(name, discription, id, status, startTime, duration);
        this.subTaskList = subTaskList;
    }

    public Epic(String name, String discription, LocalDateTime startTime, Duration duration ) {
        super(name, discription, startTime, duration);
    }

    public static Epic create(int epicId, Epic epic) {
        return new Epic(epic.name, epic.discription, epicId, new ArrayList<>(), Status.NEW, epic.startTime, epic.duration);
    }

    @Override
    public String toString() {
        return "ru.yandex.praktikum.tasks.Epic{" +
                "\n" +
                "name='" + name + '\'' +
                ", discription='" + discription + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subTasks=" + subTaskList +
                '}' +
                "\n";
    }

    public List<SubTask> getSubTaskList() {
        if (subTaskList == null){
            return subTaskList = new ArrayList<>();
        }
        return subTaskList;
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(name, epic.getName()) && Objects.equals(id, epic.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}