package ru.yandex.praktikum.tasks;

import ru.yandex.praktikum.models.Status;

public class Task {

    protected String name;
    protected String discription;
    protected int id;
    protected Enum status;

    public Task() {
    }

    public Task(String name, String discription, int id, Enum status) {
        this.name = name;
        this.discription = discription;
        this.id = id;
        this.status = status;
    }

    public Task(String name, String discription) {
        this.name = name;
        this.discription = discription;
    }

    public static Task create(int taskId, Task task) {
        return new Task(task.name, task.discription, taskId, Status.NEW);
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

    @Override
    public String toString() {
        return "ru.yandex.praktikum.tasks.Task{" +
                "name='" + name + '\'' +
                ", discription='" + discription + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}