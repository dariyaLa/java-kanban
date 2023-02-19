package ru.yandex.praktikum.taskManager;

import ru.yandex.praktikum.exception.NotFoundExeption;
import ru.yandex.praktikum.history.HistoryManager;
import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    LocalDateTime START_TIME_NOT_SET = LocalDateTime.of(LocalDate.of(1, 1, 1),
            LocalTime.of(0, 0));

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subTask) throws NotFoundExeption;

    Task createTask(Task task) throws NotFoundExeption;

    void removeTasks();

    void removeAlLSubTasks();

    void removeAllEpics();

    void removeEpicId(int taskId) throws NotFoundExeption;

    //возвращаем список подзадач эпика
    List<SubTask> getSubTaskEpic(int epicId);

    //удаление подзадачи в листе подзадач эпика
    HashMap<Integer, Epic> removeTaskSubTaskListEpic(int taskId);

    //удаление подзадачи
    void removeSubTaskId(int taskId);

    //удаление задачи
    void removeTaskId(int taskId);

    void updateSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    //рассчитываем статус эпика в зависимости от статуса задач
    Status checkStatusEpic(List<SubTask> subTaskList);

    //получаем подзадачу по идентификатору
    SubTask getSubTaskById(int taskId);

    //получаем задачу по идентификатору
    Task getTaskById(int taskId) throws NotFoundExeption;

    //получаем эпик по идентификатору
    Epic getEpicByIdClient(int taskId);

    List<Epic> getEpicHashMap();

    List<SubTask> getSubTaskHashMap();

    List<Task> getTaskHashMap();

    List<Task> getInMemoryHistoryManager();

    HashMap<Integer, Epic> setEpicHashMap();

    HashMap<Integer, SubTask> setSubTaskHashMap();

    HashMap<Integer, Task> setTaskHashMap();

    HistoryManager getInMemoryHistoryManagerDefault();

    LocalDateTime getEndTime(Task task);

    Task foundTask(int id);

    TreeSet<Task> getTaskTreeSetPrioritized();

    Epic getEpicById(int epic_id) throws NotFoundExeption;
}