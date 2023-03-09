package ru.yandex.praktikum.tasks;

import ru.yandex.praktikum.models.Status;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<SubTask> subTaskList = new ArrayList<>();
    private LocalDateTime endTime;

    LocalDateTime TIME_NOT_SET = LocalDateTime.of(LocalDate.of(1, 1, 1),
            LocalTime.of(0, 0));

    public Epic(String name, String discription, int id, List<SubTask> subTaskList, Status status, LocalDateTime startTime, Duration duration) {
        super(name, discription, id, status, startTime, duration);
        this.subTaskList = subTaskList;
        this.endTime = calculationStartEndDurationEpic();

    }

    public Epic(String name, String discription, LocalDateTime startTime, Duration duration) {
        super(name, discription, startTime, duration);
    }

    public Epic(String name, String discription) {
        super(name, discription);
    }

    public Epic(String name, String discription, Status status, LocalDateTime startTime) {
        super(name, discription, status, startTime);
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
        if (subTaskList == null) {
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

    public LocalDateTime calculationStartEndDurationEpic() {
        List<SubTask> tempList = this.subTaskList;
        if (tempList != null) {
            if (tempList.size() != 0) {

                SubTask[] tempMasSubtasks = tempList.toArray(new SubTask[0]);

                Arrays.sort(tempMasSubtasks, (first, second) -> {
                    if (first.getStartTime().isBefore(second.getStartTime())) {
                        return -1;
                    } else return 0;
                });
                //устанавливаем время начала эпика из первой задачи отсортированного списка
                this.setStartTime(tempMasSubtasks[0].getStartTime());
                //устанавливаем время конца эпика из последней задачи отсортированного списка
                this.setEndTime(getEndTime(tempMasSubtasks[tempMasSubtasks.length - 1]));
                return this.getEndTime();
            }
        }
        this.setEndTime(TIME_NOT_SET);
        this.setEndTime(TIME_NOT_SET);
        return getEndTime();

    }

    public LocalDateTime getEndTime(Task task) {
        return task.getStartTime().plus(task.getDuration());
    }
}