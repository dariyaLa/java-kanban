package ru.yandex.praktikum.tasks;

import org.junit.jupiter.api.AfterEach;
import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.httpService.KVServer;
import ru.yandex.praktikum.taskManager.HttpTaskManager;
import ru.yandex.praktikum.taskManager.TaskManager;
import ru.yandex.praktikum.utilits.Managers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer;

    {
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    TaskManager loadInServer() throws NotFoundExeption, IOException {
        kvServer.start();
        //кладем данные на сервер
        TaskManager taskManager = Managers.getDefaultServer("http://localhost:8078");
        Epic epic = new Epic("Эпик 1", "Эпик 1 Описание",
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        taskManager.createEpic(epic);
        SubTask subTaskOneEpicOne = new SubTask("Подзадача 1", "Подзадача 1 Описание", 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(11, 0)), Duration.ofMinutes(60));
        SubTask subTaskTwoEpicOne = new SubTask("Подзадача 2", "Подзадача 2 Описание", 1,
                LocalDateTime.of(LocalDate.of(2022, 2, 20),
                        LocalTime.of(10, 0)), Duration.ofMinutes(60));
        taskManager.createSubTask(subTaskOneEpicOne);
        taskManager.createSubTask(subTaskTwoEpicOne);
        Task task = new Task("Задача 1", "Описание задачи 1",
                LocalDateTime.of(LocalDate.of(2022, 2, 20), LocalTime.of(12, 0)), Duration.ofMinutes(60));
        taskManager.createTask(task);
        return taskManager;
    }

    @Override
    public TaskManager getManager() throws IOException, InterruptedException, NotFoundExeption {
        loadInServer();
        HttpTaskManager httpTaskManagerload = HttpTaskManager.fromServer("http://localhost:8078");
        return httpTaskManagerload;
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
    }
}