package ru.yandex.praktikum.tasks;

import ru.yandex.praktikum.models.Status;

import java.time.*;
import java.util.Objects;

public class Task {

    protected String name;
    protected String discription;
    protected int id;
    protected Enum status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected final LocalDateTime START_TIME_NOT_SET = LocalDateTime.of(LocalDate.of(1, 1, 1),
            LocalTime.of(0, 0));

    public Task() {
    }

    public Task(String name, String discription, int id, Enum status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.discription = discription;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String discription, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.discription = discription;
        this.startTime = startTime;
        this.duration = duration;
    }

    public static Task create(int taskId, Task task) {
        return new Task(task.name, task.discription, taskId, Status.NEW, task.startTime, task.duration);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Enum getStatus() {
        return status;
    }

    public void setStatus(Enum status) {
        this.status = status;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        if (this.startTime == null) {
            return startTime = START_TIME_NOT_SET;
        } else return startTime;
    }

    @Override
    public String toString() {
        return "ru.yandex.praktikum.tasks.Task{" +
                "name='" + name + '\'' +
                ", discription='" + discription + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}