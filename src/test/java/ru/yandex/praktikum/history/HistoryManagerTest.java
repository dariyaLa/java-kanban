package ru.yandex.praktikum.history;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.taskManager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.utilits.Managers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    public TaskManager getManager() throws NotFoundExeption {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание",
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        taskManager.createEpic(epic);
        SubTask subTaskOneEpicOne = new SubTask("Подзадача 1", "Подзадача 1 Описание", 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(9, 0)), Duration.ofMinutes(60));
        SubTask subTaskTwoEpicOne = new SubTask("Подзадача 2", "Подзадача 2 Описание", 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(11, 0)), Duration.ofMinutes(60));
        taskManager.createSubTask(subTaskOneEpicOne);
        taskManager.createSubTask(subTaskTwoEpicOne);
        Task task = new Task("Задача 1", "Описание задачи 1",
                LocalDateTime.of(LocalDate.of(2022, 2, 20), LocalTime.of(10, 0)), Duration.ofMinutes(60));
        taskManager.createTask(task);
        return taskManager;
    }

    //тест добавления в историю задачи
    @Test
    public void add() throws NotFoundExeption {
        final int INDEX_TASK = 1;
        TaskManager manager = getManager();
        manager.getEpicByIdClient(INDEX_TASK);
        List<Task> historyList = manager.getInMemoryHistoryManager();
        assertEquals(1, historyList.size(), "Неверное количество задач в истории, ожидается 1");
    }

    //тест пустой истории
    @Test
    public void emptyHistory() throws NotFoundExeption {
        TaskManager manager = getManager();
        List<Task> historyList = manager.getInMemoryHistoryManager();
        assertEquals(0, historyList.size(), "Неверное количество задач в истории, ожидается 0");
    }

    //тест проверяет, что задачи не дублируются в истории
    @Test
    public void addTwoTask() throws NotFoundExeption {
        final int INDEX_TASK = 1;
        TaskManager manager = getManager();
        manager.getEpicByIdClient(INDEX_TASK);
        manager.getEpicByIdClient(INDEX_TASK);
        List<Task> historyList = manager.getInMemoryHistoryManager();
        assertEquals(1, historyList.size(), "Неверное количество задач в истории, ожидается 1");
    }

    //тест удаления из истории задачи (начало списка)
    @Test
    public void removeTest() throws NotFoundExeption {
        final int INDEX_TASK = 1;
        TaskManager manager = getManager();
        manager.getEpicByIdClient(INDEX_TASK);
        manager.getInMemoryHistoryManagerDefault().removeTask(INDEX_TASK);
        List<Task> historyList = manager.getInMemoryHistoryManager();
        assertEquals(0, historyList.size(), "Неверное количество задач в истории, ожидается 0");
    }

    //тест удаления из истории задачи (конец списка)
    @Test
    public void removeEndListTest() throws NotFoundExeption {
        final int ID_EPIC = 1;
        final int ID_SUBTASK = 2;
        TaskManager manager = getManager();
        manager.getEpicByIdClient(ID_EPIC);
        manager.getSubTaskById(ID_SUBTASK);
        manager.getInMemoryHistoryManagerDefault().removeTask(ID_SUBTASK);
        List<Task> historyList = manager.getInMemoryHistoryManager();
        assertEquals(1, historyList.size(), "Неверное количество задач в истории, ожидается 1");
        //проверяем, что в истории остался эпик
        assertEquals(ID_EPIC, historyList.get(0).getId(), "В истории задач находится не задача с ID=1");
    }

    //тест удаления из истории задачи (середина списка)
    @Test
    public void removeCentreListTest() throws NotFoundExeption {
        final int ID_EPIC = 1;
        final int ID_SUBTASK = 2;
        final int ID_SUBTASK_TWO = 3;
        TaskManager manager = getManager();
        manager.getEpicByIdClient(ID_EPIC);
        manager.getSubTaskById(ID_SUBTASK);
        manager.getSubTaskById(ID_SUBTASK_TWO);
        manager.getInMemoryHistoryManagerDefault().removeTask(ID_SUBTASK);
        List<Task> historyList = manager.getInMemoryHistoryManager();
        assertEquals(2, historyList.size(), "Неверное количество задач в истории, ожидается 2");
        //проверяем, что в истории остался эпик и одна подзадача
        assertEquals(ID_EPIC, historyList.get(0).getId(), "В истории задач не находится задача с ID=1");
        assertEquals(ID_SUBTASK_TWO, historyList.get(1).getId(), "В истории задач не находится задача с ID=3");
    }
}