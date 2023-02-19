package ru.yandex.praktikum.tasks;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.taskManager.TaskManager;
import ru.yandex.praktikum.utilits.Managers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    void checkStatusEpicWithoutSubtasks(){

        List<SubTask> subTasksList = new ArrayList<>();
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание", 1, subTasksList, null, null, null);
        epic.setStatus(taskManager.checkStatusEpic(epic.getSubTaskList()));

        assertEquals(Status.NEW, epic.getStatus(),
                "Статус эпика без сабтасков не равен NEW");
    }

    @Test
    void checkStatusEpicAllSubtasksWithStatusNew(){
        List<SubTask> subTasksList = new ArrayList<>();
        subTasksList.add(new SubTask("Задача 1", "Описание задачи 1", 1, Status.NEW, 1, null, null));
        subTasksList.add(new SubTask("Задача 2", "Описание задачи 2", 2, Status.NEW, 1, null, null));
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание", 1, subTasksList, null, null, null);
        epic.setStatus(taskManager.checkStatusEpic(epic.getSubTaskList()));

        assertEquals(Status.NEW, epic.getStatus(),
                "Статус эпика с сабтасками в статусе NEW не равен NEW");
    }

    @Test
    void checkStatusEpicAllSubtasksWithStatusDone(){
        List<SubTask> subTasksList = new ArrayList<>();
        subTasksList.add(new SubTask("Задача 1", "Описание задачи 1", 1, Status.DONE, 1, null, null));
        subTasksList.add(new SubTask("Задача 2", "Описание задачи 2", 2, Status.DONE, 1, null, null));
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание", 1, subTasksList, null, null, null);
        epic.setStatus(taskManager.checkStatusEpic(epic.getSubTaskList()));

        assertEquals(Status.DONE, epic.getStatus(),
                "Статус эпика с сабтасками в статусе DONE не равен DONE");
    }

    @Test
    void checkStatusEpicAllSubtasksWithStatusNewAndDone(){
        List<SubTask> subTasksList = new ArrayList<>();
        subTasksList.add(new SubTask("Задача 1", "Описание задачи 1", 1, Status.NEW, 1, null, null));
        subTasksList.add(new SubTask("Задача 2", "Описание задачи 2", 2, Status.DONE, 1, null, null));
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание", 1, subTasksList, null, null, null);
        epic.setStatus(taskManager.checkStatusEpic(epic.getSubTaskList()));

        assertEquals(Status.IN_PROGRESS, epic.getStatus(),
                "Статус эпика с сабтасками в статусе NEW и DONE не равен IN_PROGRESS");
    }

    @Test
    void checkStatusEpicAllSubtasksWithStatusInProgress(){
        List<SubTask> subTasksList = new ArrayList<>();
        subTasksList.add(new SubTask("Задача 1", "Описание задачи 1", 1, Status.IN_PROGRESS, 1, null, null));
        subTasksList.add(new SubTask("Задача 2", "Описание задачи 2", 2, Status.IN_PROGRESS, 1, null, null));
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание", 1, subTasksList, null, null, null);
        epic.setStatus(taskManager.checkStatusEpic(epic.getSubTaskList()));

        assertEquals(Status.IN_PROGRESS, epic.getStatus(),
                "Статус эпика с сабтасками в статусе IN_PROGRESS не равен IN_PROGRESS");
    }

}