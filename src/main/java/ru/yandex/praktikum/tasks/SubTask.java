package ru.yandex.praktikum.tasks;

import ru.yandex.praktikum.models.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String discription, int id, Status status, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, discription, id, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String discription, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, discription, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask() {

    }

    public static SubTask create(SubTask subTask, int subTaskId) {
        return new SubTask(subTask.name, subTask.discription, subTaskId, Status.NEW, subTask.epicId, subTask.startTime, subTask.duration);
    }

    @Override
    public String toString() {
        return "ru.yandex.praktikum.tasks.SubTask{" +
                "\n" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", discription='" + discription + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}' +
                "\n";
    }

    public int getEpicId() {
        return epicId;
    }

    //для проверки обновления используем этот метод,
    //задаем новые параметры для задачи
    public int setEpicId(int epicId) {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}