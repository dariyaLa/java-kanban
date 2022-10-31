package ru.yandex.praktikum.taskManager;

import ru.yandex.praktikum.models.Status;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void createTask(Task task);

    void removeTasks();

    void removeAlLSubTasks();

    void removeAllEpics();

    void removeEpicId(int taskId);

    //чистим хешмапу сабтасков от удаленных сабтасков
    void removeSubTasksHashMap(List<SubTask> subTaskList);

    //обновляем лист подзадач в эпике
    HashMap<Integer, Epic> updateListSubTaskEpic(SubTask subTask);

    //возвращаем список подзадач эпика
    List<SubTask> getSubTaskEpic(List<Epic> epicList, int epicId);

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
    Task getTaskById(int taskId);

    //получаем эпик по идентификатору
    Epic getEpicById(List<Epic> epicList, int taskId);

    List<Epic> getEpicHashMap();

    List<SubTask> getSubTaskHashMap();

    List<Task> getTaskHashMap();
}