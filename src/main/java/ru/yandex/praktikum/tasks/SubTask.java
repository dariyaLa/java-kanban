package ru.yandex.praktikum.tasks;

import ru.yandex.praktikum.models.Status;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String discription, int id, Enum status, int epicId) {
        super(name, discription, id, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String discription, int epicId) {
        super(name, discription);
        this.epicId = epicId;
    }

    public SubTask() {
    }

    public static SubTask create(SubTask subTask, int subTaskId) {
        return new SubTask(subTask.name, subTask.discription, subTaskId, Status.NEW, subTask.epicId);
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
}